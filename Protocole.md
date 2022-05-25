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
    "sessionName": "<nom de la session>",
    "runnersCnt": <nombre de coureurs de la session>,
    "runner1": {
      "name": "<Nom + Initiale du prénom du coureur>"
    },
    "runner2": {
      "name": "<Nom + Initiale du prénom du coureur>"
    },
    ...
  }
}
```

#### timeRun

Permet de transmettre le temps d'une course spécifiée (uni-directionnel)
Non utilisé dans la version 1.4 du protocole

```json
{
  "command": "timeRun",
  "data": {
    "id": <id de la course>,
    "time1": "<temps sous forme de chaîne de caractères>",
    "wind1": "<vent sous forme de chaîne de caractères>",
    "speed1": "<vitesse sous forme de chaîne de caractères>",
    "time2": "<temps sous forme de chaine de caractères>",
    "wind2": "<vent sous forme de chaîne de caractères>",
    "speed2": "<vitesse sous forme de chaîne de caractères>"
  }
}
```

#### sessionTransfert

Permet de transmettre l'ensemble de la session (uni-directionnel).

Utilisé dans la version 1.4 du protocole comme moyen de transmission des données de course

```json
{
  "command": "sessionTransfert",
  "data": {
    "runCnt": <nombre de courses effectuées>,
    "run1": {
      "runId": "<ID (ou ligne) de la course>",
      "runner1": "<nom + initiale prénom coureur>",
      "time1": "<temps coureur 1, ou N/A>",
      "wind1": "<vitesse vent coureur 1 ou N/A>",
      "speed1": "<vitesse coureur 1 N/A>",
      "runner2": "<nom + initiale prénom coureur>",
      "time2": "<temps coureur 2 ou N/A>",
      "wind2": "<vitesse vent coureur 2 ou N/A>",
      "speed2": "<vitesse coureur 2 ou N/A>"
    },
    "run2": {
      "runId": "<ID (ou ligne) de la course>",
      "runner1": "<nom + initiale prénom coureur>",
      "time1": "<temps coureur 1, ou N/A>",
      "wind1": "<vitesse vent coureur 1 ou N/A>",
      "speed1": "<vitesse coureur 1 N/A>",
      "runner2": "<nom + initiale prénom coureur>",
      "time2": "<temps coureur 2 ou N/A>",
      "wind2": "<vitesse vent coureur 2 ou N/A>",
      "speed2": "<vitesse coureur 2 ou N/A>"
    },
    "run3": {
      "runId": "<ID (ou ligne) de la course>",
      "runner1": "<nom + initiale prénom coureur>",
      "time1": "<temps coureur 1, ou N/A>",
      "wind1": "<vitesse vent coureur 1 ou N/A>",
      "speed1": "<vitesse coureur 1 N/A>",
      "runner2": "<nom + initiale prénom coureur>",
      "time2": "<temps coureur 2 ou N/A>",
      "wind2": "<vitesse vent coureur 2 ou N/A>",
      "speed2": "<vitesse coureur 2 ou N/A>"
    },
    ...
  }
}
```

#### getCsv

Permet de demander un fichier CSV contenant l'ensemble de la course (uni-directionnel)

```json
{
  "command": "getCsv"
}
```

#### transfertCsv

Permet de transmettre les données pour le fichier CSV à la tablette (uni-directionnel)

```json
{
  "command": "transfertCsv",
  "data": {
    "runnersCnt": <nombre de coureurs>,
    "runner1": {
      "idRun": <id course>,
      "name": "<nom coureur>",
      "time": "<temps en CDC>",
      "wind": "<vent en CDC>",
      "speed": "<vitesse en CDC>"
    },
    "runner2": {
      "idRun": <id course>,
      "name": "<nom coureur>",
      "time": "<temps en CDC>",
      "wind": "<vent en CDC>",
      "speed": "<vitesse en CDC>"
    },
    ...
  }
}
```

#### authCheck

Permet de vérifier les identifiants de connection (bi-directionnel)

Cas Application vers Raspberry:
```json
{
  "command": "authCheck",
  "data": {
    "login": "<identifiant>",
    "pass": "<mot de passe>"
  }
}
```

Cas Raspberry vers Application
```json
{
  "command": "authCheck",
  "data": {
    "success": <O ou 1 si réussi>
  }
}
```


#### btnState

Permet de transmettre l'état des boutons lors d'une action

```json
{
  "command": "btnState",
  "data": {
    "btnSess": <0 ou 1>,
    "btnPrep": <0 ou 1>,
    "btnAVM": <0 ou 1>,
    "btnReady": <0 ou 1>,
    "btnGo": <0 ou 1>,
    "btnStop": <0 ou 1>
  }
}
```


#### getControl

Permet de reprendre la main en cas d'utilisation d'un périphérique de contrôle différent (bi-directionnel)

```json
{
  "command": "getControl"
}
```
