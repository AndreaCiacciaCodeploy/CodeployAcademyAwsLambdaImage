# CodeployAccademyAwsLambdaImage
Questo piccolo progetto serve per introdurre il servizio Aws Lambda.
Quando un file viene (png/jpg) viene caricato su un bucket s3, viene avviata una lambda aws che effettua una resize dell'immagine e un successivo caricamento del file generato in un bucket diverso da quello iniziale.

Di seguito un'immagine riassuntiva del flusso implementato
```
![alt awslambdaimage](https://github.com/AndreaCiacciaCodeploy/CodeployAccademyAwsLambdaImage/blob/develop/extra/codeployawslambdaimage.png?raw=true)
```

# prerequisiti
- account aws
- definizione di un ruolo che permetta di agire su S3 e Aws Lambda
- creazione di 1 o 2 bucket s3

# crearzione lambda 
Creare la lambda con le sequenti impostazioni di base:

- Runtime Java8 
- Handler com.codeploy.accademy.awslambda.awslambda.CodeployLambdaFunctionHandler::handleRequest (package, classe e nome del metodo con handler dell'evento scatenante)

La lambda utilizza inoltre delle variabili d'ambiente impostate in aws.
Dalla Console, nella sezione Environment variables, impostare le seguenti variabili

**DEST_BUCKET** (indcare il nome del bucket di destinazione)
**DEST_FOLDER** (indicare eventuale folder di destinazione)
**RESIZE_HEIGHT** (l'altezza da impostare durante il resize)
**RESIZE_WIDTH** (la larghezza da impostare durante il resize)
**PUBLIC** (valorizzare con true o false. Se true il file caricato dopo la resize sará accessibile pubblicamente)
