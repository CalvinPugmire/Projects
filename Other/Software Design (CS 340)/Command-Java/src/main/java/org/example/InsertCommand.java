package org.example;

public class InsertCommand implements Command {
    private IDocument _document;
    private int insertionIndex;
    private String sequenceInput;

    InsertCommand (IDocument _document, int insertionIndex, String sequenceInput) {
        this._document = _document;
        this.insertionIndex = insertionIndex;
        this.sequenceInput = sequenceInput;
    }

    @Override
    public void execute() {
        _document.insert(insertionIndex, sequenceInput);
    }

    @Override
    public void undo() {
        _document.delete(insertionIndex, sequenceInput.length());
    }
}
