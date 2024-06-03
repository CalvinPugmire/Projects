package org.example;

public class OpenCommand implements Command {
    private IDocument _document;
    private String openFileName;
    private String oldState;

    OpenCommand(IDocument _document, String openFileName) {
        this._document = _document;
        this.openFileName = openFileName;
    }

    @Override
    public void execute() {
        oldState = _document.sequence().toString();
        _document.open(openFileName);
    }

    @Override
    public void undo() {
        _document.clear();
        _document.insert(0, oldState);
    }
}
