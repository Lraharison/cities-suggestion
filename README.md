# API REST de suggestion de place
Cette application est basée sur AWS Lambda et le service REST est exposé via le service API Gateway d'AWS.
Le déploiement de l'application et la mise à jour de l'infrastructure sont pris en charge par le framework [serverless](https://serverless.com/framework/docs/)

## Pré-requis

 - Configurer le CLI [AWS](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-configure.html)  
 - Installer [serverless](https://serverless.com/framework/docs/providers/aws/guide/installation/)

## Déploiement

 - Builder le projet java :
 `mvn clean install`
 
 - Déployer sur AWS:
 `sls deploy`
A la fin du déploiement l'URL du service créé s'affichera sur l'écran comme suit : 
`endpoints:
  GET - https://<KEY>.execute-api.ca-central-1.amazonaws.com/dev/suggestions`
  
 
## Pour tester : [https://obo6punjvj.execute-api.ca-central-1.amazonaws.com/dev/suggestions?q=Londo&latitude=43.70011&longitude=-79.4163](https://obo6punjvj.execute-api.ca-central-1.amazonaws.com/dev/suggestions?q=Londo&latitude=43.70011&longitude=-79.4163)


