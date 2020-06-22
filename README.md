
<p align="center">
  June 2020 PS Interview Assessment
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
sbt run examples/program.mow
```

### Solution

La stratégie basique pour parser un fichier par paires de lignes éventuellement invalides consiste à tenter d'extraire une position ou une liste de commande à chaque ligne. Cette stratégie présente l'intérêt de pouvoir être étendue au cas d'un stream avec beaucoup d'instructions venant en continu.

On suppose que la tondeuse ne change pas de coordonnées si on essaie de la faire bouger hors de la zone permise par la pelouse.

Avec des notations matricielles, si on considère la matrice de rotation r(theta), le vecteur unitaire direction v(k), la position de la tondeuse p(k),
En notant k l'état courant, l'état k+1 suivant est défini dans le système de coordonnées (x, y) par:

```
           | [[ 0 1],
           | [-1 0]]    // si theta = -pi/2 (rotation à droite)
r(theta) = |
           | [[0 -1],
           | [1  0]]    // si theta = +pi/2 (rotation à gauche)

v(k+1) = r(theta) @ v(k)  // shape (2, 1)
p(k+1) = p(k) + v(k+1)    // shape (2, 1)
```

### Livrable

Nous implémentons un objet [*Mower*](src/main/scala/mowitnow/Mower.scala) capable de :
- parser un fichier texte d'instructions,
- écrire les données au format de chaîne de caractères,
- calculer les positions finales à l'aide d'un algorthme itératif.

```
sbt:mower> run examples/program.mow
[info] Running mowitnow.Mower examples/program.mow
Pelouse de taille 5x5
1 3 N
5 1 E
[success] Total time: 0 s, completed Sep 1, 2019 7:01:40 PM
```

Un mini système d'intégration continue est également mis en place dans [Travis CI](http://travis-ci.org/mycaule/ps-assessment).
