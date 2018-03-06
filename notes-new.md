


`docker build -f Dockerfile-web -t index.alauda.cn/claas/piggy-new-web .`

`docker run --name piggy-web -d -p 8080:80 index.alauda.cn/claas/piggy-new-web`