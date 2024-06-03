const cookieParser = require('cookie-parser');
const bcrypt = require('bcrypt');
const express = require('express');
const app = express();
const DB = require('./database.js');
const { PeerProxy } = require('./peerProxy.js');

const authCookieName = 'token';

// The service port may be set on the command line
const port = process.argv.length > 2 ? process.argv[2] : 4000;

// JSON body parsing using built-in middleware
app.use(express.json());

// Use the cookie parser middleware for tracking authentication tokens
app.use(cookieParser());

// Serve up the applications static content
app.use(express.static('public'));

// Router for service endpoints
var apiRouter = express.Router();
app.use(`/api`, apiRouter);

// CreateAuth token for a new user
apiRouter.post('/auth/create', async (req, res) => {
  if (await DB.getUser(req.body.username)) {
    res.status(409).send({ msg: 'Existing user' });
  } else {
    const user = await DB.createUser(req.body.username, req.body.password);

    // Set the cookie
    setAuthCookie(res, user.token);

    res.send({
      id: user._id,
    });
  }
});

// GetAuth token for the provided credentials
apiRouter.post('/auth/login', async (req, res) => {
  const user = await DB.getUser(req.body.username);
  if (user) {
    if (await bcrypt.compare(req.body.password, user.password)) {
      setAuthCookie(res, user.token);
      res.send({ id: user._id });
      return;
    }
  }
  res.status(401).send({ msg: 'Unauthorized' });
});

// DeleteAuth token if stored in cookie
apiRouter.delete('/auth/logout/:username', (_req, res) => {
  res.clearCookie(authCookieName);
  res.status(204).end();
});

apiRouter.delete('/auth/exit/:username', async (_req, res) => {
  const user = await DB.updateUser(_req.params.username, null, null, "");
  res.status(204).end();
});

// GetUser returns information about a user
apiRouter.get('/user/:username', async (req, res) => {
  const user = await DB.getUser(req.params.username);
  if (user) {
    const token = req?.cookies.token;
    res.send({ username: user.username, authenticated: token === user.token, 
      wins: user.wins, losses: user.losses, game: user.game});
    return;
  }
  res.status(404).send({ msg: 'Unknown' });
});

apiRouter.get('/lobbycount/:game', async (req, res) => {
  const count = await DB.getLobbyCount(req.params.game);
  if (count >= 0) {
    res.send({ lobbyCount: count });
    return;
  }
  res.status(404).send({ msg: 'Unknown' });
});

apiRouter.post('/auth/join', async (req, res) => {
  const host = await DB.getUser(req.body.host);
  if (host) {
    if (host.game === host.username) {
      const user = await DB.updateUser(req.body.username, null, null, req.body.host);
      res.send({ id: host._id });
      return;
    }
  }
  res.status(401).send({ msg: 'Not hosting' });
});

apiRouter.post('/auth/host', async (req, res) => {
  const user = await DB.updateUser(req.body.username, null, null, req.body.host);
  res.send({ id: user._id });
  return;
});

// secureApiRouter verifies credentials for endpoints
var secureApiRouter = express.Router();
apiRouter.use(secureApiRouter);

secureApiRouter.use(async (req, res, next) => {
  const authToken = req.cookies[authCookieName];
  const user = await DB.getUserByToken(authToken);
  if (user) {
    next();
  } else {
    res.status(401).send({ msg: 'Unauthorized' });
  }
});

// Default error handler
app.use(function (err, req, res, next) {
  res.status(500).send({ type: err.name, message: err.message });
});

// Return the application's default page if the path is unknown
app.use((_req, res) => {
  res.sendFile('logreg.html', { root: 'public' });
});

// setAuthCookie in the HTTP response
function setAuthCookie(res, authToken) {
  res.cookie(authCookieName, authToken, {
    secure: true,
    httpOnly: true,
    sameSite: 'strict',
  });
}

const httpService = app.listen(port, () => {
  console.log(`Listening on port ${port}`);
});

new PeerProxy(httpService);
