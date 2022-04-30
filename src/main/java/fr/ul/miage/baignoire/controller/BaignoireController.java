package fr.ul.miage.baignoire.controller;

import fr.ul.miage.baignoire.models.Baignoire;
import fr.ul.miage.baignoire.services.FuiteService;
import fr.ul.miage.baignoire.services.ProgressService;
import fr.ul.miage.baignoire.services.RemplissageService;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class BaignoireController {

    @FXML
    TextArea console;
    @FXML
    ProgressBar baignoireProgress;
    @FXML
    ProgressBar fuiteProgress;
    @FXML
    ProgressBar fuiteProgress2;
    @FXML
    TextField volumeBaignoire;
    @FXML
    TextField debitRobinet;
    @FXML
    Spinner<Integer> nbTrous;
    @FXML
    Button start;
    @FXML
    Button stop;
    @FXML
    FlowPane trous;

    private ArrayList<TextField> trousValues = new ArrayList<>();
    private FuiteService fuiteService;
    private RemplissageService remplissageService;
    private ProgressService progressRemplissage;
    private ProgressService progressFuite;
    private ProgressService progressFuite2;
    private Baignoire baignoire;

    private Instant startTime;

    @FXML
    private void initialize() {
        this.initSpinner();
        this.addListeners();
    }

    private void addListeners() {
        this.nbTrous.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.trous.getChildren().clear();
            this.trousValues = new ArrayList<TextField>();
            for (int i = 0; i < newValue; i++) {
                TextField txt = new TextField();
                txt.setPromptText(String.format("Trou N°%d", i + 1));
                this.trousValues.add(txt);
                trous.getChildren().add(txt);
            }
        });
        this.start.setOnAction(e -> {
            if (this.verifFields()) {
                stopServices();
                this.baignoire = new Baignoire(Integer.parseInt(volumeBaignoire.getText()),
                        Integer.parseInt(debitRobinet.getText()), this.nbTrous.getValue());
                this.trousValues.forEach(x -> {
                    this.baignoire.ajouterTrou(Integer.parseInt(x.getText()));
                });
                this.remplissageService = new RemplissageService(baignoire);
                remplissageService.setOnSucceeded((WorkerStateEvent event) -> {
                    Double valueProgress = ((double) (remplissageService.getValue()) / (double) (this.baignoire.getVolume()));
                    this.baignoireProgress.setProgress(valueProgress);
                    this.console.setText("");
                    this.console.appendText(String.format("Volume final de la baignoire : %d \n", this.baignoire.getVolumeActuel()));
                    this.console.appendText(String.format("Eau consommée totale : %d \n", this.baignoire.getConso()));
                    this.console.appendText(String.format("Eau qui a débordée : %d \n", this.baignoire.getDebordage()));
                    this.console.appendText(String.format("Eau qui a fuit : %d \n", this.baignoire.getFuite()));
                });
                this.fuiteService = new FuiteService(baignoire);
                fuiteService.setOnSucceeded((WorkerStateEvent event) -> {
                    Double valueProgress = ((double) (fuiteService.getValue()) / (double) (this.baignoire.getVolume()));
                    this.baignoireProgress.setProgress(valueProgress);
                    this.console.setText("");
                    this.console.appendText(String.format("Volume final de la baignoire : %d \n", this.baignoire.getVolumeActuel()));
                    this.console.appendText(String.format("Eau consommée totale : %d \n", this.baignoire.getConso()));
                    this.console.appendText(String.format("Eau qui a débordée : %d \n", this.baignoire.getDebordage()));
                    this.console.appendText(String.format("Eau qui a fuit : %d \n", this.baignoire.getFuite()));
                });
                this.progressFuite = new ProgressService(fuiteProgress);
                this.progressFuite2 = new ProgressService(fuiteProgress2);
                progressFuite2.start();
                if (this.nbTrous.getValue() > 0) {
                    progressFuite.start();
                }
                remplissageService.start();
                fuiteService.start();
                this.startTime = Instant.now();
            } else {
                this.console.setText("VEUILLEZ SAISIR CORRECTEMENT LES VALEURS");
            }
        });
        this.stop.setOnAction(e -> {
            stopServices();
            this.console.setText("");
            if (this.startTime != null){
                this.console.appendText(String.format("Simulation Terminée - Durée du remplissage : %d secondes \n", Duration.between(startTime, Instant.now()).toSeconds()));
                this.startTime = null;
            }
            this.console.appendText(String.format("Volume final de la baignoire : %d \n", this.baignoire.getVolumeActuel()));
            this.console.appendText(String.format("Eau consommée totale : %d \n", this.baignoire.getConso()));
            this.console.appendText(String.format("Eau qui a débordée : %d \n", this.baignoire.getDebordage()));
            this.console.appendText(String.format("Eau qui a fuit : %d \n", this.baignoire.getFuite()));
        });
    }

    private void initSpinner() {
        nbTrous.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10));
    }

    private boolean verifFields() {
        try {
            if (this.volumeBaignoire.getText().isEmpty() || this.volumeBaignoire.getText().isBlank()) {
                return false;
            }
            int temp = Integer.parseInt(this.volumeBaignoire.getText());
            if (temp <= 0 || temp > 100000) {
                return false;
            }
            if (this.debitRobinet.getText().isEmpty() || this.debitRobinet.getText().isBlank()) {
                return false;
            }
            temp = Integer.parseInt(this.debitRobinet.getText());
            if (temp <= 0 || temp > 100000) {
                return false;
            }
            boolean[] condition = new boolean[1];
            condition[0] = true;
            this.trousValues.forEach(x -> {
                int trou = Integer.parseInt(x.getText());
                if (trou <= 0 || trou > 10000) {
                    condition[0] = false;
                }
            });
            return condition[0];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void stopServices(){
        try {
            remplissageService.cancel();
            remplissageService.reset();
            fuiteService.cancel();
            fuiteService.reset();
            progressFuite.cancel();
            progressFuite.reset();
            fuiteProgress.setProgress(0);
            progressFuite2.cancel();
            progressFuite2.reset();
            fuiteProgress2.setProgress(0);
        } catch (IllegalStateException | NullPointerException excep) {
            this.console.appendText("ILLEGAL STATE \n");
        }
    }
}
