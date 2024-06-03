package org.example;

public class StartCommand implements Command {
    private IDocument _document;
    private String oldState;

    StartCommand(IDocument _document) {
        this._document = _document;
    }

    @Override
    public void execute() {
        oldState = _document.sequence().toString();
        _document.clear();
    }

    @Override
    public void undo() {
        _document.insert(0, oldState);
    }
}
