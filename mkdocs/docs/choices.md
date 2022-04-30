# Choices

## Description

Dans cette partie je décrirais les choix que j'ai fait lors du développement de l'application.

### Conception de l'interface

Afin de construire l'interface, j'ai décidé de séparer l'application en trois parties : 

- Le formulaire et les champs dans la partie du haut
- La "simulation" au milieu de l'application
- La console / logs en bas de l'application.

Le formulaire est créé dans un grid pane, avec deux textFields pour le volume de la baignoire et le débit de remplissage,
un spinner pour choisir le nombre de trous, et des textFields dans un flowPane pour ajouter le débit de chaque trou.

Pour la "simulation", j'ai utilisé un AnchorPane qui m'a permis de positionner facilement mes éléments via SceneBuilder.
J'ai ajouté quelques formes pour créer le robinet, et la modélisation des trous, de la baignoire et du remplissage s'est faite via une barre de progression que j'actualise à l'aide d'un Service.

Pour la console j'ai décidé d'utiliser un simple textArea me permettant de pouvoir afficher des informations.

### La conception d'objets

Pour la partie conception d'objets, j'ai utilisé trois objets pour ma simulation : 
- Baignoire
- Robinet
- Trou

La classe baignoire possède toutes les fonctionnalités de remplissage, d'ajout d'eau, de créations de trous etc....
Elle possède notamment en attributs : son volume total, son volume actuel, sa consommation totale d'eau, l'eau qui a débordée, l'eau qui a fuitée...
Elle possède aussi une liste de trous qui correspondent aux fuites de la baignoire. Elle possède aussi un robinet afin d'avoir l'information sur le remplissage de la baignoire

La classe robinet et la classe trou sont sensiblement les mêmes, elles ne contiennent qu'un attribut correspondant à leur valeur.

### Les choix et les principes de parallélisations des tâches

Afin de paralléliser la simulation, j'ai décidé d'utiliser les services "UnscheduledService", vus en cours.

J'en ai notamment trois : 
- FuiteService
- ProgressService
- RemplissageService

FuiteService s'occupe des fuites et appelle la fonction viderBaignoire de l'objet Baignoire, le service retourne le volume actuel de la baignoire.

ProgressService permet simplement d'actualiser la progression des barres de progression afin de faire une animation de fuite d'eau.

RemplissageService s'occupe du remplissage de la baignoire avec la fonction augmenterVolume, le service retourne le volume actuel de la baignoire.

J'ai préféré ce choix de conception à la création de Runnable dans une pool car je trouvais cette conception plus propre et plus simple à comprendre.

Les deux services en lien avec la simulation s'effectue toutes les deux secondes, le service pour les barres de progression s'effectue toutes les 100 millisecondes.