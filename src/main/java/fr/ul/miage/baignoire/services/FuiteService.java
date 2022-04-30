package fr.ul.miage.baignoire.services;

import fr.ul.miage.baignoire.models.Baignoire;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class FuiteService extends ScheduledService<Integer> {

    private Baignoire baignoire;

    public FuiteService(Baignoire baignoire){
        super();
        this.baignoire = baignoire;
        this.setPeriod(Duration.seconds(2));
    }

    @Override
    protected Task<Integer> createTask() {
        return new Task<Integer>(){
            protected Integer call(){
                baignoire.viderBaignoire();
                return baignoire.getVolumeActuel();
            }
        };
    }
}
