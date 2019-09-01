
<p align="center">
  September 2019 PS Interview Assessment
</p>

<p align="center">
  <a href="http://travis-ci.org/mycaule/ps-assessment"><img src="https://api.travis-ci.org/mycaule/ps-assessment.svg?branch=master" alt="Build Status"></a>
  <br>
  <br>
</p>


### Utilisation
```
# Lancer les tests unitaires
sbt test
# Lancer le programme
sbt run
```

### Solution

### Livrable

Nous implémentons un objet [*Mower*](src/main/scala/mycaule/Mower.scala) capable de :
- parser un fichier texte d'instructions,
- écrire les données au format de chaîne de caractères,
- calculer les positions finales à l'aide d'un algorthme itératif.

```
[info] Running mowitnow.Mower
Positions finales :
1 3 N
5 1 E
```

Un mini système d'intégration continue est également mis en place dans [Travis CI](http://travis-ci.org/mycaule/ps-assessment).

### Explications

... TODO
