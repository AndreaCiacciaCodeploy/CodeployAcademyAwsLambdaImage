# CodeployAccademyAwsLambdaImage
Questo piccolo progetto serve per introdurre il servizio Aws Lambda.
Quando un file viene (png/jpg) viene caricato su un bucket s3, viene avviata una lambda aws che effettua una resize dell'immagine e un successivo caricamento del file generato in un bucket diverso da quello iniziale.

# prerequisiti
- account aws
- definizione di un ruolo che permetta di agire su S3 e Aws Lambda
- creazione di 1 o 2 bucket s3


![alt text](https://image.3bmeteo.com/images/newarticles/w_255/6281ae90e924066a1f1940e9b25d9b9f_sole.jpg)
