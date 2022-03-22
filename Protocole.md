# Protocole de communication

## Type de protocole:

Protocole TCP avec formatage JSON

## Le Protocole

### Modèle de base d'une trame protocolaire

```json
{
  "command": "<command>",
  "data": {
    "<some data>": "<data>"
  }
}
```

### Liste des commandes disponibles:

#### transfertRunner

Permet de transmettre la liste des coureurs sélectionnés pour une course (bi-directionnel)

```json
{
  "command": "transferRunner",
  "data": {
    "id": <numéro de course>,
    "runners": [
      <id coureur 1>,
      <id coureur 2>
    ]
  }
}
```

#### transfertAllRunners

Permet de transmettre l'ensemble des coureurs disponibles à l'application (uni-directionnel)

```json
{
  "command": "transfertAllRunners",
  "data": {
    "runnersCnt": <nombre de coureurs de la session>,
    "runner1": {
      "id": <id du coureur dans la BDD>,
      "name": "<Nom + Initiale du prénom du coureur>"
    },
    "runner2": {
      "id": <id coureur dans la BDD>,
      "name": "<Nom + Initiale du prénom du coureur>"
    },
    ...
  }
}
```
