# 🔒 PermiTrack

**Seu auditor de permissões Android — visualize, entenda e controle o que seus aplicativos podem acessar.**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22+-blue.svg)](https://kotlinlang.com)
[![Jetpack Compose](https://img.shields.io/badge/Compose-1.7.0-brightgreen.svg)](https://developer.android.com/jsix)

## 📋 Visão Geral

O PermiTrack ajuda os usuários a compreenderem exatamente quais permissões estão concedendo aos seus aplicativos instalados. Em vez de aceitar cegamente os pedidos de permissões, você pode agora:

- Visualizar todos os aplicativos instalados e suas permissões
- Identificar permissões de risco alto e crítico
- Entender o que cada permissão significa
- Gerar relatórios de privacidade para compartilhamento
- Funcionar completamente offline — seus dados nunca deixam seu dispositivo

## 🌟 Funcionalidades Principais

| Funcionalidade | Descrição |
|---------------|-----------|
| 📱 **Inventário de Aplicativos** | Lista todos os apps instalados com suas permissões completas |
| 🏷️ **Classificação de Permissões** | Agrupa permissões por categorias de risco (Baixo, Médio, Alto, Crítico) |
| ⚠️ **Identificação de Riscos** | Detecta automaticamente permissões perigosas e desnecessárias |
| 📜 **Histórico de Alterações** | Rastreia quando permissões são adicionadas ou removidas |
| 📄 **Relatórios de Privacidade** | Gera relatórios detalhados em formato legível |
| 🌐 **Funcionamento Offline** | Todos os dados processados e armazenados localmente no dispositivo |
| 📦 **Exportação .zip** | Projeto pronto para publicação em repositórios Git |

## 📱 Telas do Aplicativo

### Tela Principal
Visão geral com estatísticas rápidas sobre seus aplicativos, incluindo contagem de permissões e distribuição de risco.

<img src="readme-assets/screenshots/main-screen.png" alt="Tela Principal do PermiTrack" width="300"/>

### Lista de Aplicativos
Grid scrollável mostrando todos os aplicativos instalados com indicadores de risco visuais.

<img src="readme-assets/screenshots/app-list.png" alt="Lista de Aplicativos do PermiTrack" width="300"/>

### Detalhe da Permissão
Visualização detalhada de todas as permissões de um aplicativo específico, com rótulos de risco coloridos.

<img src="readme-assets/screenshots/permission-detail.png" alt="Detalhe da Permissão do PermiTrack" width="300"/>

### Análise de Risco
Gráficos e estatísticas mostrando a distribuição de risco em todos os seus aplicativos.

<img src="readme-assets/screenshots/risk-analysis.png" alt="Análise de Risco do PermiTrack" width="300"/>

### Relatório de Privacidade
Tela para gerar e visualizar relatórios de privacidade que podem ser compartilhados.

<img src="readme-assets/screenshots/privacy-report.png" alt="Relatório de Privacidade do PermiTrack" width="300"/>

## 🛠️ Tecnologias Utilizadas

- **Kotlin** - Linguagem principal
- **Jetpack Compose** - Toolkit moderno para UI nativa em Android
- **Room** - Camada de banco de dados SQLite robusta e tipo-safe
- **Android SDK** - Para acesso às permissões e informações dos aplicativos

## 📦 Como Executar o Projeto

```bash
# Clonar o repositório
git clone https://github.com/seu-usuario/permittrack.git

# Entrar no diretório
cd permittrack

# Abrir no Android Studio
# Ou construir via Gradle
./gradlew assembleDebug
```

## 🏗️ Arquitetura do Projeto

```
PermiTrack/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/permitrack/permittrack/
│   │ │   ├── MainActivity.kt           # Entry point da aplicação
│   │ │   ├── PermiTrackApplication.kt  # Inicialização do banco de dados
│   │ │   ├── database/
│   │ │   │   └── PermissionDatabase.kt # Configuração Room
│   │ │   ├── model/
│   │ │   │   ├── AppInfo.kt            # Entidade de aplicativo
│   │ │   │   ├── PermissionInfo.kt      # Entidade de permissão
│   │ │   │   └── RiskLevel.kt           # Níveis de risco enumerados
│   │ │   ├── database/dao/
│   │ │   │   ├── AppInfoDAO.kt          # Data Access Object para apps
│   │ │   │   └── PermissionInfoDAO.kt   # Data Access Object para permissões
│   │ │   ├── viewmodel/
│   │ │   │   └── MainViewModel.kt       # Lógica de UI
│   │ │   ├── ui/
│   │ │   │   ├── theme/
│   │ │   │   │   └── PermiTrackTheme.kt # Tema Material Design
│   │ │   │   ├── navigation/
│   │ │   │   │   └── PermiTrackNavHost.kt # Navigation Compose
│   │ │   │   ├── screens/
│   │ │   │   │   ├── MainScreen.kt      # Tela inicial
│   │ │   │   │   ├── AppListScreen.kt   # Lista de aplicativos
│   │ │   │   │   ├── PermissionDetailScreen.kt # Detalhe permissões
│   │ │   │   │   ├── RiskAnalysisScreen.kt   # Análise de risco
│   │ │   │   │   └── PrivacyReportScreen.kt # Relatório privacidade
│   │ │   │   └── MainActivity.kt          # Activity principal
│   │   └── res/
│   │       └── (resources)
│   ├── build.gradle.kts
│   └── ...
└── README.md
```

## 📜 Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🙏 Créditos

Desenvolvido com ❤️ por desenvolvedores Android. Inspirado nas necessidades de transparência de permissões dos usuários modernos.

---

**PermiTrack** — Porque suas permissões, suas regras.