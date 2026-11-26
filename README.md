# PermiTrack 0.2.0

> Auditor offline de permissões Android, construído para tornar o acesso solicitado por aplicativos mais compreensível.

## O que mudou

A versão 0.2.0 transforma a base inicial em um projeto Android estruturado: usa `PackageManager` para inventariar aplicativos visíveis, lê permissões declaradas e o estado concedido, classifica riscos com regras explícitas, persiste resultados com Room/KSP e mantém histórico de mudanças entre varreduras. O dashboard mostra dados reais, com pesquisa, atualização manual, estados de carregamento e erro.

Os resultados usam linguagem cuidadosa: “potencialmente excessiva” significa apenas que a permissão é sensível e merece revisão no contexto do aplicativo; não é uma afirmação de que o acesso é desnecessário.

## Critérios de risco

`CRITICAL` cobre SMS, gravação de áudio, localização precisa e localização em segundo plano. `HIGH` cobre câmera, contatos, registros de chamadas, armazenamento legado e conexão Bluetooth. `MEDIUM` cobre outras permissões perigosas e dados de calendário/telefone. `LOW` cobre permissões normais ou desconhecidas. As regras estão em `PermissionRiskClassifier.kt` e são deliberadamente determinísticas, offline e auditáveis.

## Privacidade e limitações do Android

O projeto não usa internet, rastreamento, telemetria, credenciais ou chaves privadas. Não solicita armazenamento: o relatório é exportado pelo Storage Access Framework, escolhido pelo usuário. Backups automáticos estão desabilitados para evitar cópia inadvertida do banco local.

`QUERY_ALL_PACKAGES` é declarado somente no manifesto e nunca solicitado em tempo de execução, pois é uma permissão especial de visibilidade. O Android e o Google Play restringem seu uso a casos cuja função principal realmente exige conhecer todos os aplicativos instalados; uma publicação real deve avaliar a política vigente e, quando suficiente, usar `<queries>` para declarar apenas os pacotes necessários. O inventário também filtra aplicativos de sistema nesta primeira versão.

## Estrutura

```text
app/src/main/java/com/permitrack/app/
├── MainActivity.kt
├── PermiTrackApplication.kt
├── data/
│   ├── PermiTrackDatabase.kt
│   ├── PermissionRepository.kt
│   └── PermissionRiskClassifier.kt
└── model/Models.kt
```

## Build

O projeto contém configurações Kotlin DSL, SDK 35, AGP 8.7.3, Kotlin 2.0.21, Compose 1.7.6, Room 2.6.1 e KSP. O wrapper aponta para Gradle 8.9. Nesta sessão não foi possível executar Gradle/Android SDK: o ambiente de Chat não disponibiliza um executor de comandos nem um SDK verificável. Portanto, o APK e os testes não foram executados aqui; a árvore entregue é fonte, não um build validado.

## Imagens

Não foram incluídos screenshots: o aplicativo não pôde ser executado neste ambiente, e nenhum mockup é apresentado como captura real.

## Licença

MIT.

## Development status

Version 0.2.0 is a development preview.

- Build and automated tests have not been validated.
- The official Gradle Wrapper is incomplete.
- Gradle 8.9, JDK 17 and Android SDK 35 are required.
- The application ID has changed to com.permitrack.app.
- A complete security audit is still pending.
