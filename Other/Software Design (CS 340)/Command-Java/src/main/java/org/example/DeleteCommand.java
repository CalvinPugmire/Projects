package org.example;

public class DeleteCommand implements Command {
    private IDocument _document;
    private int deletionIndex;
    private int deletionDistance;
    private String deletedString;

    DeleteCommand (IDocument _document, int deletionIndex, int deletionDistance) {
        this._document = _document;
        this.deletionIndex = deletionIndex;
        this.deletionDistance = deletionDistance;
    }

    @Override
    public void execute() {
        deletedString = _document.delete(deletionIndex, deletionDistance);
    }

    @Override
    public void undo() {
        _document.insert(deletionIndex, deletedString);
    }
}
