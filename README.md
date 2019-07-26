# CodeployAcademyAwsLambdaImage
Questo piccolo progetto serve per introdurre il servizio Aws Lambda.
Quando un file viene (png/jpg) viene caricato su un bucket s3, viene avviata una lambda aws che effettua una resize dell'immagine e un successivo caricamento del file generato in un bucket diverso da quello iniziale.

Di seguito un'immagine riassuntiva del flusso implementato

<p align="center">
  <img src="https://github.com/AndreaCiacciaCodeploy/CodeployAcademyAwsLambdaImage/blob/develop/extra/codeployawslambdaimage.png?raw=true">
</p>

# prerequisiti
- account aws
- definizione di un ruolo che permetta di agire su S3 e Aws Lambda
- creazione di 1 o 2 bucket s3

# creazione lambda 
Creare la lambda con le sequenti impostazioni di base:

- Runtime Java8 
- Handler com.codeploy.academy.awslambda.CodeployLambdaFunctionHandler::handleRequest (package, classe e nome del metodo con handler dell'evento scatenante)

Abilitare il trigger associando il servizio AWS S3 e il metodo di PUT (usato quando un file viene caricato sul bucket).

La lambda utilizza inoltre delle variabili d'ambiente impostate in aws.
Dalla Console, nella sezione Environment variables, impostare le seguenti variabili

**DEST_BUCKET** (indcare il nome del bucket di destinazione)<br/>
**DEST_FOLDER** (indicare eventuale folder di destinazione)<br/>
**RESIZE_HEIGHT** (l'altezza da impostare durante il resize)<br/>
**RESIZE_WIDTH** (la larghezza da impostare durante il resize)<br/>
**PUBLIC** (valorizzare con true o false. Se true il file caricato dopo la resize sará accessibile pubblicamente)<br/>
**FILEOUT_RENAME** (valorizzare con true o false. Se true il file caricato verrá rinominato con un suffisso rappresentante il timestamp)<br/>

Di seguito uno screen per completezza.

<p align="center">
  <img src="https://github.com/AndreaCiacciaCodeploy/CodeployAcademyAwsLambdaImage/blob/develop/extra/lambda.png?raw=true">
</p>

Compilare e deployare il codice eseguibile dalla console (per ulteriore semplicitá nella cartella extra é presente il codice eseguibile .jar)
Testare il tutto caricando un file sul bucket di origine.

Per monitorare l'esecuzione é possibile visualizzare i log su CloudWatch Logs.
Quando l'esecuzione é terminata, il file di output, sará presente nel bucket di destinazione.
