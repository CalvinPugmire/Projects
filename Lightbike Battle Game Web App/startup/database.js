const { MongoClient } = require('mongodb');
const bcrypt = require('bcrypt');
const uuid = require('uuid');

const userName = process.env.MONGOUSER;
const password = process.env.MONGOPASSWORD;
const hostname = process.env.MONGOHOSTNAME;

if (!userName) {
  throw Error('Database not configured. Set environment variables');
}

const url = `mongodb+srv://${userName}:${password}@${hostname}`;

const client = new MongoClient(url);
const userCollection = client.db('lightbikebattle').collection('user');

function getUser(username) {
  return userCollection.findOne({ username: username });
}

function getUserByToken(token) {
  return userCollection.findOne({ token: token });
}

function getLobbyCount(game) {
  return userCollection.count({ game: game });
}

async function createUser(username, password) {
  // Hash the password before we insert it into the database
  const passwordHash = await bcrypt.hash(password, 10);

  const user = {
    username: username,
    password: passwordHash,
    token: uuid.v4(),
    wins: 0,
    losses: 0,
    game: "",
  };
  await userCollection.insertOne(user);

  return user;
}

async function updateUser(username, wins, losses, game) {
  const user0 = await getUser(username);

  if (wins === null) {
    wins = user0.wins;
  }
  if (losses === null) {
    losses = user0.losses;
  }
  if (game === null) {
    game = user0.game;
  }

  const user = {
    username: user0.username,
    password: user0.password,
    token: user0.token,
    wins: wins,
    losses: losses,
    game: game,
  };
  await userCollection.findOneAndReplace({username: user.username}, user);

  return user;
}

setInterval(gameClearLoop, 300000);

function gameClearLoop () {
  userCollection.updateMany({ },{$set:{game:''}});
}

module.exports = {
  getUser,
  getUserByToken,
  getLobbyCount,
  createUser,
  updateUser,
};
