# Entrega en GitHub

1. Crear un repositorio en tu cuenta de GitHub, por ejemplo AdminCES-Automation.
2. Copiar el enlace de ese repositorio.
3. Abrir una terminal en la carpeta que contiene automation-framework y este archivo.
4. Ejecutar los comandos siguientes, reemplazando URL_DE_TU_REPOSITORIO por el enlace real.

```powershell
git init
git add .
git commit -m "Agregar scripts AdminCES con IBrowser e IVerify"
git branch -M main
git remote add origin URL_DE_TU_REPOSITORIO
git push -u origin main
```

Estas instrucciones corresponden a la carpeta nueva extraída del ZIP. Si se integra en un repositorio que ya tiene Git, conservar sus remotos y trabajar en una rama propia.

El archivo adminces.properties no se publica: está excluido por .gitignore. Se publica solamente adminces.properties.example, sin credenciales.

Verificar en GitHub que se muestran automation-framework/src, pom.xml y ADMINCES.md. El enlace de tu repositorio es el entregable de la actividad.
