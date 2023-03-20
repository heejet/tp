package seedu.clialgo.command;

import java.util.Objects;
import java.io.File;

import seedu.clialgo.FileType;
import seedu.clialgo.TopicManager;
import seedu.clialgo.storage.FileManager;
import seedu.clialgo.Ui;

/**
 * The <code>AddCommand</code> objects represents the user command to add new notes into CLIAlgo.
 */
public class AddCommand extends Command {

    protected final String name;
    protected final Ui ui;
    protected final String topic;

    /**
     * Constructor for command to add note to topic list.
     *
     * @param name Name of the note file.
     * @param topic The topic that this file is tagged to.
     */
    public AddCommand(String name, String topic) {
        this.name = name;
        this.topic = topic;
        this.ui = new Ui();
    }

    String getName() {
        return this.name;
    }

    String getTag() {
        return this.topic;
    }

    /**
     * Checks if the file exists as a .txt or .cpp.
     *
     * @return A <code>FileType</code> enum that determines whether the file DOESNOTEXIST, is a TXT or CPP file.
     */
    public FileType checkFileType() {
        String pathInTxt = this.name + ".txt";
        String pathInCpp = this.name + ".cpp";

        if (new File(".\\" + pathInTxt).isFile()) {
            return FileType.TXT;
        } else if (new File(".\\" + pathInCpp).isFile()) {
            return FileType.CPP;
        } else {
            return FileType.DOESNOTEXIST;
        }
    }

    /**
     * An overridden method to execute the user command to add new notes into CLIAlgo.
     *
     * @param topicManager The <code>TopicManager</code> object which handles all notes stored in CLIAlgo.
     * @param ui The <code>Ui</code> object which handles outputs to the user.
     * @param fileManager The <code>FileManager</code> object responsible for saving information in CLIAlgo.
     */
    @Override
    public void execute(TopicManager topicManager, Ui ui, FileManager fileManager) {
        boolean isTestModeOn = topicManager.getIsTestModeOn();

        // Check if the file exists
        if ((checkFileType() == FileType.DOESNOTEXIST) && !isTestModeOn) {
            ui.printFileDoesNotExist();
            return;
        }

        // Check if topicName is valid
        if (!topicManager.isValidTopic(topic)) {
            new InvalidTopicCommand(topic).execute(topicManager, ui, fileManager);
            return;
        }
        // Check if the note is repeated
        if (topicManager.isRepeatedNote(name)) {
            assert topicManager.isRepeatedNote(name);
            ui.printNoteExists();
            return;
        }

        if (checkFileType() == FileType.TXT) {
            new AddNoteCommand(name, topic).execute(topicManager, ui, fileManager);
        } else if (checkFileType() == FileType.CPP) {
            new AddCodeCommand(name, topic).execute(topicManager, ui, fileManager);
        }

        assert this.name != null;
        assert this.topic != null;
        assert topicManager.isValidTopic(topic);
        // Ui for successful adding
        ui.printAddSuccess(name, topic);
    }

    /**
     * An overridden method that checks for equality of <code>AddCommand</code> objects.
     *
     * @param otherCommand The other <code>AddCommand</code> object to be checked against.
     * @return A boolean value to determine whether the <code>AddCommand</code> objects are equal.
     */
    @Override
    public boolean equals(Command otherCommand) {
        AddCommand otherAddCommand = (AddCommand) otherCommand;

        return Objects.equals(this.getName(), otherAddCommand.getName()) &&
                Objects.equals(this.getTag(), otherAddCommand.getTag());
    }
}
