#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <unistd.h>

#define pi 3.14
void attente(int nbSec); 

int delaiUniforme(int temps, int delta); 

int delaiGauss(double moyenne, double ecartype); 

int delaiExponentiel(double lambda); 