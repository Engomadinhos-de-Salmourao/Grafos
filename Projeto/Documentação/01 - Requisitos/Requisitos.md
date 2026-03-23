# Requisitos do Sistema

## Requisitos Funcionais

| ID   | Requisito |
|------|-----------|
| RF01 | O sistema deve permitir que o usuário informe o destino da viagem. |
| RF02 | O sistema deve permitir que o usuário informe o orçamento disponível para a viagem. |
| RF03 | O sistema deve permitir que o usuário informe o período ou tempo disponível para a viagem. |
| RF04 | O sistema deve permitir que o usuário informe ou atualize suas preferências pessoais, como interesses em gastronomia, cultura, lazer ou natureza. |
| RF05 | O sistema deve gerar um roteiro de viagem com base no destino, no orçamento, no período e nas preferências informadas pelo usuário. |
| RF06 | O sistema deve permitir que o usuário visualize os detalhes do roteiro gerado. |
| RF07 | O sistema deve permitir que o usuário ajuste o roteiro gerado, incluindo alteração de ordem, inclusão, remoção ou modificação do tempo de permanência nos locais. |
| RF08 | O sistema deve permitir que o usuário salve roteiros para consulta futura. |
| RF09 | O sistema deve permitir que o usuário visualize seus roteiros salvos. |
| RF10 | O sistema deve permitir que o usuário exclua roteiros salvos. |
| RF11 | O sistema deve permitir que o usuário visualize detalhes de um local, como categoria, custo estimado e tempo médio de permanência. |
| RF12 | O sistema deve permitir que o usuário registre locais já visitados. |
| RF13 | O sistema deve considerar os locais já visitados no perfil do usuário para apoiar futuras personalizações ou recomendações. |
| RF14 | O sistema deve permitir que o administrador cadastre novas cidades no sistema. |
| RF15 | O sistema deve permitir que o administrador cadastre e gerencie locais de interesse associados às cidades disponíveis. |

## Requisitos Não Funcionais

| ID    | Requisito |
|-------|-----------|
| RNF01 | O sistema deve apresentar interface simples e intuitiva, permitindo que o usuário realize as principais operações sem necessidade de treinamento prévio. |
| RNF02 | O sistema deve gerar roteiros em tempo adequado, mesmo com múltiplos locais cadastrados para uma cidade. |
| RNF03 | O sistema deve armazenar os roteiros, preferências e histórico do usuário de forma persistente para acessos futuros. |
| RNF04 | O sistema deve garantir que apenas administradores autorizados possam cadastrar ou alterar cidades e locais disponíveis no sistema. |
| RNF05 | O sistema deve validar os dados fornecidos pelo usuário, impedindo valores inválidos de orçamento, período e preferências. |
| RNF06 | O sistema deve ser desenvolvido de forma modular, permitindo futura expansão para novas cidades, categorias de locais e regras de recomendação. |
| RNF07 | O sistema deve manter consistência dos dados, evitando duplicidade ou perda de informações salvas. |
| RNF08 | O sistema deve permitir evolução futura para integração com APIs externas de mapas, turismo e localização. |
| RNF09 | O sistema deve ser compatível com ambiente web, permitindo acesso por navegador. |
| RNF10 | O sistema deve proteger os dados do usuário, especialmente preferências pessoais e histórico de roteiros. |
| RNF11 | O sistema deve poder ser executado em contêineres, de forma a facilitar padronização de ambiente, testes e implantação. |
| RNF12 | O sistema deve ser preparado para implantação em ambiente de nuvem, permitindo escalabilidade e maior disponibilidade da aplicação. |
| RNF13 | O processo de deploy do sistema deve ser passível de automação, facilitando atualizações e manutenção do serviço. |
| RNF14 | O sistema deve permitir configuração externa de variáveis de ambiente, credenciais e parâmetros operacionais, sem necessidade de alteração no código-fonte. |
| RNF15 | O sistema deve possibilitar monitoramento básico da aplicação, incluindo disponibilidade do serviço e identificação de falhas em execução. |