1. API: Listar archivos de un directorio
   - Método: POST
   - URL: http://localhost:8080/lista
   - Cuerpo (JSON):

     {
       "directorio": "/ruta/al/directorio",
       "parametro": "20230531"
     }


2. API: Buscar archivos en el directorio según un parámetro
   - Método: POST
   - URL: http://localhost:8080/all
   - Resultado: ArchivosAll.zip
   - Cuerpo (JSON):

     {
       "directorio": "/ruta/al/directorio",
       "parametro": "20230531"
     }
Se puede usar comodin * para incluir todo, para buscar por extensión se pone "parametro": "txt", solo incluira los archivos con extensión txt.

3. API: Buscar archivos en un directorio según los nombres proporcionados
   - Método: POST
   - URL: http://localhost:8080/especifica
   - Resultado: ArchivosEspe.zip
   - Cuerpo (JSON):

     {
       "directorio": "/ruta/al/directorio",
       "nombres": ["archivo1.txt", "archivo2.txt", "archivo3.txt"]
     }

Para buscar un archivo sin conocer la extensión, solo se pone el nombre completo y sera incluido en el zip final.
