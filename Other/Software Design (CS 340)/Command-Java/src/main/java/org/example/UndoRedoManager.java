package org.example;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedoManager {
    Deque<Command> undoStack = new ArrayDeque<>();
    Deque<Command> redoStack = new ArrayDeque<>();

    public void execute (Command command) {
        command.execute();
        undoStack.addFirst(command);
        redoStack.clear();
    }

    public void undo () {
        Command command = undoStack.removeFirst();
        command.undo();
        redoStack.addFirst(command);
    }

    public void redo () {
        Command command = redoStack.removeFirst();
        command.execute();
        undoStack.addFirst(command);
    }

    public boolean canUndo () {
        if (undoStack.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canRedo () {
        if (redoStack.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
