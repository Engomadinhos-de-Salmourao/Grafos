@startuml
left to right direction
skinparam classAttributeIconSize 0
skinparam packageStyle rectangle
skinparam shadowing false

package "Camada de Apresentação" {

  package "View" {
    class RoteiroView {
      + exibirRoteiro(roteiro: Roteiro)
      + exibirDetalhesDestino(destino: Destino)
      + exibirDetalhesLugar(lugar: Lugar)
      + exibirMensagem(mensagem: String)
    }
  }

  package "Controllers" {
    class RoteiroController {
      + gerarRoteiro(destinoId: Long, orcamento: double, periodoDias: int, usuarioId: Long): Roteiro
      + ajustarRoteiro(roteiroId: Long): Roteiro
      + salvarRoteiro(roteiroId: Long): void
      + excluirRoteiro(roteiroId: Long): void
      + listarRoteiros(usuarioId: Long): List<Roteiro>
    }

    class UsuarioController {
      + visualizarPerfil(usuarioId: Long): Usuario
      + listarRoteiros(usuarioId: Long): List<Roteiro>
      + registrarLugarVisitado(usuarioId: Long, lugarId: Long): void
    }

    class PerfilController {
      + atualizarPreferencias(usuarioId: Long, preferencias: PreferenciaUsuario): void
      + visualizarPreferencias(usuarioId: Long): PreferenciaUsuario
    }
  }
}

package "Camada de Aplicação" {

  package "Application Services" {
    class GerarRoteiroService {
      + gerarRoteiro(destinoId: Long, orcamento: double, periodoDias: int, usuarioId: Long): Roteiro
      + montarRoteiro(usuario: Usuario, destino: Destino, lugares: List<Lugar>): Roteiro
    }

    class GerenciarRoteiroService {
      + salvarRoteiro(roteiro: Roteiro): void
      + excluirRoteiro(roteiroId: Long): void
      + buscarRoteirosPorUsuario(usuarioId: Long): List<Roteiro>
      + ajustarRoteiro(roteiro: Roteiro): Roteiro
    }

    class GerenciarUsuarioService {
      + buscarUsuarioPorId(usuarioId: Long): Usuario
      + registrarLugarVisitado(usuarioId: Long, lugar: Lugar): void
    }

    class GerenciarPreferenciaService {
      + atualizarPreferencias(usuarioId: Long, preferencias: PreferenciaUsuario): void
      + buscarPreferenciasPorUsuario(usuarioId: Long): PreferenciaUsuario
    }
  }

  package "Ports" {
    interface RoteiroRepositoryPort {
      + salvar(roteiro: Roteiro): void
      + excluir(roteiroId: Long): void
      + buscarPorId(roteiroId: Long): Roteiro
      + buscarPorUsuario(usuarioId: Long): List<Roteiro>
    }

    interface UsuarioRepositoryPort {
      + buscarPorId(usuarioId: Long): Usuario
      + salvar(usuario: Usuario): void
    }

    interface DestinoRepositoryPort {
      + buscarPorId(destinoId: Long): Destino
      + listarTodos(): List<Destino>
      + salvar(destino: Destino): void
    }

    interface LugarRepositoryPort {
      + buscarPorId(lugarId: Long): Lugar
      + buscarPorDestino(destinoId: Long): List<Lugar>
      + salvar(lugar: Lugar): void
    }

    interface PreferenciaRepositoryPort {
      + buscarPorUsuario(usuarioId: Long): PreferenciaUsuario
      + salvar(preferencia: PreferenciaUsuario): void
    }

    interface RoteirizacaoPort {
      + gerarRoteiro(usuario: Usuario, destino: Destino, lugares: List<Lugar>, orcamento: double, periodoDias: int): Roteiro
    }
  }
}

package "Camada de Infraestrutura" {

  package "Adapters" {
    class RoteiroRepositoryAdapter {
      + salvar(roteiro: Roteiro): void
      + excluir(roteiroId: Long): void
      + buscarPorId(roteiroId: Long): Roteiro
      + buscarPorUsuario(usuarioId: Long): List<Roteiro>
    }

    class UsuarioRepositoryAdapter {
      + buscarPorId(usuarioId: Long): Usuario
      + salvar(usuario: Usuario): void
    }

    class DestinoRepositoryAdapter {
      + buscarPorId(destinoId: Long): Destino
      + listarTodos(): List<Destino>
      + salvar(destino: Destino): void
    }

    class LugarRepositoryAdapter {
      + buscarPorId(lugarId: Long): Lugar
      + buscarPorDestino(destinoId: Long): List<Lugar>
      + salvar(lugar: Lugar): void
    }

    class PreferenciaRepositoryAdapter {
      + buscarPorUsuario(usuarioId: Long): PreferenciaUsuario
      + salvar(preferencia: PreferenciaUsuario): void
    }

    class AlgoritmoRoteirizacaoAdapter {
      + gerarRoteiro(usuario: Usuario, destino: Destino, lugares: List<Lugar>, orcamento: double, periodoDias: int): Roteiro
      + calcularScoreLugar(lugar: Lugar, preferencias: PreferenciaUsuario): double
      + selecionarMelhoresLugares(lugares: List<Lugar>, orcamento: double, periodoDias: int): List<Lugar>
    }
  }
}

package "Camada de Domínio" {

  package "Entidades de Domínio" {
    class Usuario {
      - id: Long
      - nome: String
      - email: String
      - role: String
      + atualizarPreferencias(preferencias: PreferenciaUsuario): void
      + adicionarRoteiro(roteiro: Roteiro): void
      + removerRoteiro(roteiro: Roteiro): void
      + registrarLugarVisitado(lugar: Lugar): void
    }

    class PreferenciaUsuario {
      - gastronomia: double
      - cultura: double
      - natureza: double
      - vidaNoturna: double
      + atualizarPesos(gastronomia: double, cultura: double, natureza: double, vidaNoturna: double): void
      + calcularAfinidade(lugar: Lugar): double
    }

    class Roteiro {
      - id: Long
      - nome: String
      - orcamento: double
      - periodoDias: int
      - salvo: boolean
      + adicionarItem(item: ItemRoteiro): void
      + removerItem(item: ItemRoteiro): void
      + calcularCustoTotal(): double
      + calcularTempoTotal(): double
      + salvar(): void
    }

    class Destino {
      - id: Long
      - nome: String
      - estado: String
      - descricao: String
      + adicionarLugar(lugar: Lugar): void
      + removerLugar(lugar: Lugar): void
      + listarLugares(): List<Lugar>
    }

    abstract class Lugar {
      - id: Long
      - nome: String
      - descricao: String
      - custoMedio: double
      - tempoMedioPermanencia: double
      - popularidade: double
      + calcularScore(preferencias: PreferenciaUsuario): double
      + obterDetalhes(): String
    }

    class Hotel {
      + obterTipo(): String
    }

    class Restaurante {
      + obterTipo(): String
    }

    class PontoTuristico {
      + obterTipo(): String
    }

    class ItemRoteiro {
      - ordem: int
      - tempoPlanejado: double
      + alterarOrdem(novaOrdem: int): void
      + alterarTempoPlanejado(novoTempo: double): void
    }
  }
}

RoteiroView --> RoteiroController
RoteiroView --> UsuarioController
RoteiroView --> PerfilController

RoteiroController --> GerarRoteiroService
RoteiroController --> GerenciarRoteiroService
UsuarioController --> GerenciarUsuarioService
PerfilController --> GerenciarPreferenciaService

GerarRoteiroService --> RoteirizacaoPort
GerarRoteiroService --> DestinoRepositoryPort
GerarRoteiroService --> LugarRepositoryPort
GerarRoteiroService --> UsuarioRepositoryPort
GerarRoteiroService --> PreferenciaRepositoryPort
GerarRoteiroService --> RoteiroRepositoryPort

GerenciarRoteiroService --> RoteiroRepositoryPort
GerenciarUsuarioService --> UsuarioRepositoryPort
GerenciarPreferenciaService --> PreferenciaRepositoryPort

RoteiroRepositoryAdapter ..|> RoteiroRepositoryPort
UsuarioRepositoryAdapter ..|> UsuarioRepositoryPort
DestinoRepositoryAdapter ..|> DestinoRepositoryPort
LugarRepositoryAdapter ..|> LugarRepositoryPort
PreferenciaRepositoryAdapter ..|> PreferenciaRepositoryPort
AlgoritmoRoteirizacaoAdapter ..|> RoteirizacaoPort

Ports --> "Entidades de Domínio"

Roteiro --> ItemRoteiro
ItemRoteiro --> Lugar
Usuario --> PreferenciaUsuario
Usuario --> Roteiro
Roteiro --> Destino

Lugar <|-- Hotel
Lugar <|-- Restaurante
Lugar <|-- PontoTuristico

@enduml