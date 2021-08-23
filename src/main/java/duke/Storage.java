package duke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    public static final String SERIALIZATION_PATH = "data/task_list.txt";

    public static void updateLocalFile(TaskList taskList) throws IOException {

        ArrayList<Task> list = taskList.arrayList;
        FileWriter fileWriter = new FileWriter(SERIALIZATION_PATH);
        for (Task task : list) {
            StringBuilder sb = new StringBuilder();
            if (task.getClass().equals(Deadline.class)) {
                sb.append("D |");
            } else if (task.getClass().equals(Todo.class)) {
                sb.append("T |");
            } else if (task.getClass().equals(Event.class)) {
                sb.append("E |");
            }

            if (task.isDone) {
                sb.append(" 1 | ");
            } else {
                sb.append(" 0 | ");
            }

            sb.append(task.description);
            if (task.getClass().equals(Deadline.class) ) {
                Deadline dTask = (Deadline) task;
                sb.append(" | " + dTask.by);
            } else if (task.getClass().equals(Event.class)) {
                Event eTask = (Event) task;
                sb.append(" | " + eTask.at);
            }

            sb.append("\n");
            fileWriter.write(sb.toString());
        }
        fileWriter.close();
    }


    public static ArrayList<Task> load(String path) {
        ArrayList<Task> arrayList = new ArrayList<>(100);
        File taskFile = new File(path);
        if (!taskFile.getParentFile().exists()) {
            taskFile.getParentFile().mkdirs();
        }

        try {
            Scanner s = new Scanner(taskFile);
            while (s.hasNextLine()) {
                String arrayString = s.nextLine();
                String[] inputArray = arrayString.strip().split(" \\| ");
                String type = inputArray[0];
                boolean done = inputArray[1].strip().equals("1");
                String description = inputArray[2];
                if (type.equals("T")) {
                    Todo newTask = new Todo(description, done);
                    arrayList.add(newTask);
                } else if (type.equals("D")) {
                    String time = inputArray[3];
                    Deadline newTask = new Deadline(description, time, done);
                    arrayList.add(newTask);
                } else if (type.equals("E")) {
                    String time = inputArray[3];
                    Event newTask = new Event(description, time, done);
                    arrayList.add(newTask);
                } else {
                    arrayList.clear();
                    throw new DukeException("Unknown input type in file!");
                }


            }
            return arrayList;


        } catch (FileNotFoundException e) {
            Ui.printStatement("First time use, creating task file...");
            return arrayList;
        } catch (DukeException e) {
            Ui.printError(e.getMessage());
            return arrayList;
        }
    }

}