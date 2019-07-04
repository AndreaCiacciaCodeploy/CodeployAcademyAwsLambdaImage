# CodeployAccademyAwsLambdaImage
Questo piccolo progetto serve per introdurre il servizio Aws Lambda.
Quando un file viene (png/jpg) viene caricato su un bucket s3, viene avviata una lambda aws che effettua una resize dell'immagine e un successivo caricamento del file generato in un bucket diverso da quello iniziale.

# prerequisiti
- account aws
- definizione di un ruolo che permetta di agire su S3 
- creazione di 1 o 2 bucket s3
