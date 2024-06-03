package spell;

import java.io.File;
import java.util.*;

import java.io.IOException;

public class SpellCorrector implements ISpellCorrector {
    private Dictionary dictionary;

    public SpellCorrector() {
        dictionary = new Dictionary();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()) {
            if (scanner.hasNext("[A-Za-z]+")) {
                String inputText = scanner.next();
                inputText = inputText.toLowerCase();
                dictionary.add(inputText);
            } else {
                scanner.next();
            }
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (inputWord == null || inputWord.equals("")) {
            return null;
        }
        inputWord = inputWord.toLowerCase();
        INode outputNode = dictionary.find(inputWord);
        if (outputNode != null) {
            return inputWord;
        } else {
            Set<String> oneEdits = new HashSet<>();
            oneEdits.addAll(wrongCharacter(inputWord));
            oneEdits.addAll(extraCharacter(inputWord));
            oneEdits.addAll(omitCharacter(inputWord));
            oneEdits.addAll(swapCharacter(inputWord));
            Set<String> oneEditCandidates = new HashSet<>();
            for (String oneEdit : oneEdits) {
                INode oneEditCandidate = dictionary.find(oneEdit);
                if (oneEditCandidate != null) {
                    oneEditCandidates.add(oneEdit);
                }
            }
            if (oneEditCandidates.size() > 0) {
                int maxCount1 = 0;
                Set<String> oneEditSemis = new HashSet<>();
                for (String oneEditCandidate : oneEditCandidates) {
                    int oECCount = dictionary.find(oneEditCandidate).getValue();
                    if (oECCount > maxCount1) {
                        oneEditSemis.clear();
                        oneEditSemis.add(oneEditCandidate);
                        maxCount1 = oECCount;
                    } else if (oECCount == maxCount1) {
                        oneEditSemis.add(oneEditCandidate);
                    }
                }
                TreeSet<String> oneEditFinals = new TreeSet<>(oneEditSemis);
                return oneEditFinals.first();
            } else {
                Set<String> twoEdits = new HashSet<>();
                for (String oneEdit : oneEdits) {
                    twoEdits.addAll(wrongCharacter(oneEdit));
                    twoEdits.addAll(extraCharacter(oneEdit));
                    twoEdits.addAll(omitCharacter(oneEdit));
                    twoEdits.addAll(swapCharacter(oneEdit));
                }
                Set<String> twoEditCandidates = new HashSet<>();
                for (String twoEdit : twoEdits) {
                    INode twoEditCandidate = dictionary.find(twoEdit);
                    if (twoEditCandidate != null) {
                        twoEditCandidates.add(twoEdit);
                    }
                }
                if (twoEditCandidates.size() > 0) {
                    int maxCount2 = 0;
                    Set<String> twoEditSemis = new HashSet<>();
                    for (String twoEditCandidate : twoEditCandidates) {
                        int tECCount = dictionary.find(twoEditCandidate).getValue();
                        if (tECCount > maxCount2) {
                            twoEditSemis.clear();
                            twoEditSemis.add(twoEditCandidate);
                            maxCount2 = tECCount;
                        } else if (tECCount == maxCount2) {
                            twoEditSemis.add(twoEditCandidate);
                        }
                    }
                    TreeSet<String> twoEditFinals = new TreeSet<>(twoEditSemis);
                    return twoEditFinals.first();
                } else {
                    return null;
                }
            }
        }
    }

    public Set<String> wrongCharacter (String inputWord) {
        Set<String> wrongCharacters = new HashSet<>();
        for (int i = 0; i < inputWord.length(); i++) {
            for (int j = 0; j < 26; j++) {
                char letter = (char)('a' + j);
                StringBuilder builder = new StringBuilder();
                if (i > 0) {
                    builder.append(inputWord, 0, i);
                }
                builder.append(letter);
                if (i < inputWord.length()-1) {
                    builder.append(inputWord.substring(i+1));
                }
                String outputString = builder.toString();
                wrongCharacters.add(outputString);
            }
        }
        return wrongCharacters;
    }

    public Set<String> extraCharacter (String inputWord) {
        Set<String> extraCharacters = new HashSet<>();
        if (inputWord.length() > 1) {
            for (int i = 0; i < inputWord.length(); i++) {
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord, 0, i);
                if (i < inputWord.length() - 1) {
                    builder.append(inputWord.substring(i + 1));
                }
                String outputString = builder.toString();
                extraCharacters.add(outputString);
            }
        }
        return extraCharacters;
    }

    public Set<String> omitCharacter (String inputWord) {
        Set<String> omitCharacters = new HashSet<>();
        for (int i = 0; i < inputWord.length()+1; i++) {
            for (int j = 0; j < 26; j++) {
                char letter = (char)('a' + j);
                StringBuilder builder = new StringBuilder();
                if (i > 0) {
                    builder.append(inputWord, 0, i);
                }
                builder.append(letter);
                if (i < inputWord.length()) {
                    builder.append(inputWord.substring(i));
                }
                String outputString = builder.toString();
                omitCharacters.add(outputString);
            }
        }
        return omitCharacters;
    }

    public Set<String> swapCharacter (String inputWord) {
        Set<String> swapCharacters = new HashSet<>();
        if (inputWord.length() > 1) {
            for (int i = 0; i < inputWord.length()-1; i++) {
                StringBuilder builder = new StringBuilder();
                if (i > 0) {
                    builder.append(inputWord, 0, i);
                }
                builder.append(inputWord, i+1, i+2);
                builder.append(inputWord, i, i+1);
                if (i < inputWord.length() - 2) {
                    builder.append(inputWord.substring(i+2));
                }
                String outputString = builder.toString();
                swapCharacters.add(outputString);
            }
        }
        return swapCharacters;
    }
}
