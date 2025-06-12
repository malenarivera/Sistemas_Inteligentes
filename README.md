# Sistemas_Inteligentes
Repositorio destinado al segundo tpo de la materia Sistemas Inteligentes. 

Se implementa el arbol de decision c4.5 sin el uso de librerias externas. Para ello, se utiliza el dataset de "Zoo", disponible en el siguiente link: https://archive.ics.uci.edu/dataset/111/zoo. 

La implementacion esta bastante reducida, tiene implementado la parte de:
- La construccion recursiva
- Calcular la ganancia para atributos continuos (con gain ratio)
- Metodo para entrenar y otro para predecir

Falta: 
- la parte de la poda
- separar la forma de calcular la ganancia segun el tipo de atributo (discreto o continuo)
- tiene una profundidad hardcodeada
