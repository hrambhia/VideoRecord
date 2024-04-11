import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class VideoRecord {
    private String title;
    private int userRating;
    private int yearOfRelease;
    private int rentalFrequency;
    private int rentOutDuration;

    public VideoRecord(String title, int userRating, int yearOfRelease, int rentalFrequency, int rentOutDuration) {
        this.title = title;
        this.userRating = userRating;
        this.yearOfRelease = yearOfRelease;
        this.rentalFrequency = rentalFrequency;
        this.rentOutDuration = rentOutDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public int getRentalFrequency() {
        return rentalFrequency;
    }

    public void setRentalFrequency(int rentalFrequency) {
        this.rentalFrequency = rentalFrequency;
    }

    public int getRentOutDuration() {
        return rentOutDuration;
    }

    public void setRentOutDuration(int rentOutDuration) {
        this.rentOutDuration = rentOutDuration;
    }
}

class VideoRentalSystem {
    private List<VideoRecord> records;
    private final String fileName = "D:/lifesucks/mini_project/Java/VideoRecord/src/video_records.txt";

    public VideoRentalSystem() {
        records = new ArrayList<>();
    }

    public void readRecordsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0].trim();
                int userRating = Integer.parseInt(parts[1].trim());
                int yearOfRelease = Integer.parseInt(parts[2].trim());
                int rentalFrequency = Integer.parseInt(parts[3].trim());
                int rentOutDuration = Integer.parseInt(parts[4].trim());
                records.add(new VideoRecord(title, userRating, yearOfRelease, rentalFrequency, rentOutDuration));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<VideoRecord> getRecords() {
        return records;
    }

    public void addRecord(VideoRecord record) {
        records.add(record);
        saveRecordsToFile();
        updateAverages();
    }

    public void updateRecord(int index, String field, String value) {
        VideoRecord record = records.get(index);
        switch (field.toLowerCase()) {
            case "title":
                record.setTitle(value);
                break;
            case "user rating":
                try {
                    int userRating = Integer.parseInt(value);
                    record.setUserRating(userRating);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for user rating!");
                }
                break;
            case "year of release":
                try {
                    int yearOfRelease = Integer.parseInt(value);
                    record.setYearOfRelease(yearOfRelease);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for year of release!");
                }
                break;
            case "rental frequency":
                try {
                    int rentalFrequency = Integer.parseInt(value);
                    record.setRentalFrequency(rentalFrequency);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for rental frequency!");
                }
                break;
            case "rent out duration":
                try {
                    int rentOutDuration = Integer.parseInt(value);
                    record.setRentOutDuration(rentOutDuration);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for rent out duration!");
                }
                break;
            default:
                System.out.println("Invalid field name!");
                return;
        }
        saveRecordsToFile();
        updateAverages();
    }

    public void deleteRecord(int index) {
        records.remove(index);
        saveRecordsToFile();
        updateAverages();
    }

    private void saveRecordsToFile() {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (VideoRecord record : records) {
                writer.write(record.getTitle() + "," + record.getUserRating() + "," +
                        record.getYearOfRelease() + "," + record.getRentalFrequency() + "," +
                        record.getRentOutDuration() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateAverages() {
        SwingUtilities.invokeLater(() -> {
            double averageUserRating = calculateAverageUserRating();
            double averageYearOfRelease = calculateAverageYearOfRelease();
            double averageRentalFrequency = calculateAverageRentalFrequency();
            double averageRentOutDuration = calculateAverageRentOutDuration();

            Main.updateAveragesInGUI(averageUserRating, averageYearOfRelease, averageRentalFrequency,
                    averageRentOutDuration);
        });
    }

    public double calculateAverageUserRating() {
        int totalUserRating = 0;
        for (VideoRecord record : records) {
            totalUserRating += record.getUserRating();
        }
        return (double) totalUserRating / records.size();
    }

    public double calculateAverageYearOfRelease() {
        int totalYearOfRelease = 0;
        for (VideoRecord record : records) {
            totalYearOfRelease += record.getYearOfRelease();
        }
        return (double) totalYearOfRelease / records.size();
    }

    public double calculateAverageRentalFrequency() {
        int totalRentalFrequency = 0;
        for (VideoRecord record : records) {
            totalRentalFrequency += record.getRentalFrequency();
        }
        return (double) totalRentalFrequency / records.size();
    }

    public double calculateAverageRentOutDuration() {
        int totalRentOutDuration = 0;
        for (VideoRecord record : records) {
            totalRentOutDuration += record.getRentOutDuration();
        }
        return (double) totalRentOutDuration / records.size();
    }
}

public class Main {
    private static JLabel averageUserRatingLabel;
    private static JLabel averageYearOfReleaseLabel;
    private static JLabel averageRentalFrequencyLabel;
    private static JLabel averageRentOutDurationLabel;

    public static void main(String[] args) {
        VideoRentalSystem rentalSystem = new VideoRentalSystem();
        rentalSystem.readRecordsFromFile();

        JFrame frame = new JFrame("Video Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(
                new Object[] { "Title", "User Rating", "Year of Release", "Rental Frequency", "Rent Out Duration" }, 0);
        for (VideoRecord record : rentalSystem.getRecords()) {
            model.addRow(new Object[] { record.getTitle(), record.getUserRating(), record.getYearOfRelease(),
                    record.getRentalFrequency(), record.getRentOutDuration() });
        }
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Record");
        JButton updateButton = new JButton("Update Record");
        JButton deleteButton = new JButton("Delete Record");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        // buttonPanel.add(refreshButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = JOptionPane.showInputDialog("Enter Title:");
                int userRating = Integer.parseInt(JOptionPane.showInputDialog("Enter User Rating:"));
                int yearOfRelease = Integer.parseInt(JOptionPane.showInputDialog("Enter Year of Release:"));
                int rentalFrequency = Integer.parseInt(JOptionPane.showInputDialog("Enter Rental Frequency:"));
                int rentOutDuration = Integer.parseInt(JOptionPane.showInputDialog("Enter Rent Out Duration:"));

                VideoRecord newRecord = new VideoRecord(title, userRating, yearOfRelease, rentalFrequency,
                        rentOutDuration);
                rentalSystem.addRecord(newRecord);
                updateTable(model, rentalSystem);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String[] options = { "Title", "User Rating", "Year of Release", "Rental Frequency",
                            "Rent Out Duration" };
                    String selectedField = (String) JOptionPane.showInputDialog(frame, "Select field to update:",
                            "Update Record",
                            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

                    if (selectedField != null) {
                        String inputValue = JOptionPane.showInputDialog("Enter new value:");
                        if (inputValue != null) {
                            rentalSystem.updateRecord(selectedRow, selectedField.toLowerCase(), inputValue);
                            updateTable(model, rentalSystem);
                        }
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    rentalSystem.deleteRecord(selectedRow);
                    updateTable(model, rentalSystem);
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rentalSystem.readRecordsFromFile();
                updateTable(model, rentalSystem);
            }
        });

        JPanel averagePanel = new JPanel();
        averagePanel.setLayout(new GridLayout(4, 1));
        averageUserRatingLabel = new JLabel("Average User Rating: " + rentalSystem.calculateAverageUserRating());
        averageYearOfReleaseLabel = new JLabel(
                "Average Year of Release: " + rentalSystem.calculateAverageYearOfRelease());
        averageRentalFrequencyLabel = new JLabel(
                "Average Rental Frequency: " + rentalSystem.calculateAverageRentalFrequency());
        averageRentOutDurationLabel = new JLabel(
                "Average Rent Out Duration: " + rentalSystem.calculateAverageRentOutDuration());
        averagePanel.add(averageUserRatingLabel);
        averagePanel.add(averageYearOfReleaseLabel);
        averagePanel.add(averageRentalFrequencyLabel);
        averagePanel.add(averageRentOutDurationLabel);

        frame.add(averagePanel, BorderLayout.NORTH);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static void updateAveragesInGUI(double averageUserRating, double averageYearOfRelease,
            double averageRentalFrequency, double averageRentOutDuration) {
        averageUserRatingLabel.setText("Average User Rating: " + averageUserRating);
        averageYearOfReleaseLabel.setText("Average Year of Release: " + averageYearOfRelease);
        averageRentalFrequencyLabel.setText("Average Rental Frequency: " + averageRentalFrequency);
        averageRentOutDurationLabel.setText("Average Rent Out Duration: " + averageRentOutDuration);
    }

    static void updateTable(DefaultTableModel model, VideoRentalSystem rentalSystem) {
        model.setRowCount(0);
        for (VideoRecord record : rentalSystem.getRecords()) {
            model.addRow(new Object[] { record.getTitle(), record.getUserRating(), record.getYearOfRelease(),
                    record.getRentalFrequency(), record.getRentOutDuration() });
        }
        updateAveragesInGUI(rentalSystem.calculateAverageUserRating(), rentalSystem.calculateAverageYearOfRelease(),
                rentalSystem.calculateAverageRentalFrequency(), rentalSystem.calculateAverageRentOutDuration());
    }
}
