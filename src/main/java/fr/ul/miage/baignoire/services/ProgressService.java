package fr.ul.miage.baignoire.services;

import fr.ul.miage.baignoire.models.Baignoire;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

public class ProgressService extends ScheduledService<Integer> {

    @FXML
    private ProgressBar progressBar;

    public ProgressService(ProgressBar progressBar) {
        super();
        this.progressBar = progressBar;
        this.setPeriod(Duration.millis(100));
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>(){
            protected Integer call(){
                if (progressBar.getProgress() > 0.9){
                    progressBar.setProgress(0);
                }else{
                    progressBar.setProgress(progressBar.getProgress() + 0.1);
                }
                return 0;
            }
        };
    }

}
