# Diagrma CLasses de Implementação
---
![](https://www.plantuml.com/plantuml/png/pLXBKnov3x_Ff-ZYyf__Bb3PwrQA2WjvK9K75SZUDTrYyCPjpzhkgS0cttrbjyRZCOAUuh0L3dIFzDD3iYpB_Se7S67ezKpZJMY2RPnQteQcKmxReAoP-Q_AhC11txGQl3yDmQd54F2YjUPAtMFpee2ieFqAIxmATngRg07CKcC1qCfWk5jXOrsujRCP2pG7fnsi0ZhVN9Y57KntqFmpcpMZpUQB7y0fUsPDS5PhTCtBbst-SN92s8R-VYMQ7tG0TudkHkdvCCfTTFyV-H06qEgU-0vlqA5f5VWjwh1P5VJpwqyivd2fV73WtjiMz9_Agm2TBUNqi0INpMX2Zs3bDsY_wyoFRN2VRK3bfeDReWF7u7aNLRubIWjsw-XQ-J70Z7FfMMJWhy47WNtO90vkTNdGQm5OX9Yv_a6YyLkh1oLAbw6_TN2pnwf21odzv-Y3CXHyUkQuyxhMY1JN0x33tgwnWXOPwbIhhA7rYslAxEqY2qP7OE8AC3OdQtjebRA1ty3EhLDBx2Ema6b7PgtyAI46jk0Sai6KyUn6lHwflYVMewooSmoWRz4dkVdZc-3IFfhknU3R0dO60PVMAPYNOM-8exi_ohQnMcb5V6LDSuLkhLhqKkjW7aNanxTzcaRp-qRy6UsZbjw_YqJ9jLNTVis4mEy9yZ8Ls8dOMiJWXQR2IgkYiww57asmHEMuM62_bb8B77dKE1RxfIMr51MiKVdvCjRcRX9hwqJGkvceuqyMDb71kO9OIxSseFu3F9BsPFAANI0Df38Ppeek0lRiBwjps89k1ptgt4IOvQhmrg1L1-wTTJ6PRmQJ3AKOnYrosDoElDDOJr5-OQr6C4ytNg4g62ILGuuWxx8ClxHKPBkAcICqRz63keSB5JRPlREsoGvU2KnqSKRjrSmhiDZG1UXqKsVmCtgxQ4SM5EArYu1ZZnSJapX5bIMedBpwL91NE1UNV1OaXIb9Uvq9TJCJXAPEY5f3VLwSsnwKaKu8YWnDKE4k1tQakgZDckNuqzWUYN0LdDgGaBJfYkQinhIbD8w4eg4HlJWxs3AByLuMD_1HrVMaFCcdKs6ydcQJCAlarbJD0vMlSfQMrWTu2wJIKeOKbFqiyyy67GOdyzx3FJgHyn582EyBiIhK-N9hbweW6Q4N7JKUDeR5vO5xUi6vm8Z6bJgIysb2Z7VndOK36l_u8H8tdAq3oQqXukUA_kqETZLvSmwT3JxOQnj0psF5TtPOQDo5N6E_UWJIWsj9X-tjLNHgNkKd-oxcoBeEUrAeJ1W98HgwHDU3uHelZHsbkjfj0w03ElAm4fmdJPKpdFbdxSuEVUjK2pRJwyCcpL9Hyh39OE6E8faNYuF3HIzD9Qd3sosFk_g9U5GbFT8LOkBUmCV8rFr06iFPT1eFf0QUaWABs3lQmDb9l6SjJcxPDJsMzcloheVELhMK2kG3Tkg9UjlCLWjqt76kLR_BUuFjxJYGbLovB98xXqsqJ8zAUIPaZNNrfFvGv6VRXD0781KceCHEa_7ru9GFghKb8aDuKONaZykSLwuiruf8wa78OOgMobfR7huhVagAp9SocStaF2O5ELLeXZwTbnp0bwlNdzFBwVc7YuyFpd0gqB2vv2aJqDr0YvjZ_3Ekh5V1khi8e-IiwC6foMCFpylLi9Ns09V_z2LGKl9ZroLkelkvv19OT6bYOES9YL3iwEHrW2Vy7PLo3AdzJKIGFouGSx-nSylEboYsp-wHooYgujDpxpi0LqxzaV7J4AIlZ5K5KQ0k__ZlQgWQL2OS-ZahY2CuTNyFQcsvYEGmVluoIgWLE8ySpxo5NfbRMtk5JgLrvNjLzedFDox7_pNRJso-EJh97wvc3xzp7n6RgOoQyK9Ke96QGSc12X9f6P2kQHMWlEX5D_ar-PYQxcpNbIGazbq1A7MI6GSphL02Caa4JbjEn2bKoiWKY3AL6qkTgQBeZu-_JmHNW-K0XJt54e-wDzvw6uSwVIdDtZK7lnrGMEaXtFNeKCSRPV01_CNnyIy7x5w4jKw13E81aJQIYYuD6JKkOBefaxT-KBaMIgVEyzGIi9S0QRuYThwJFGH5D_ATXlsrFHaDbaox6RCgpTbnPMAdutgHVNEuSkZH162Kd8BM6Z8bGV8ibq4pfZ3kPCUbSRvx4i9DR3RZ0VqxsKgvAWZb35gGgzbpzWfDD_JwNm00)
---
```plantuml
@startuml
left to right direction
skinparam classAttributeIconSize 0
skinparam packageStyle rectangle
skinparam linetype ortho

package "Adapters Inbound" {

  class UsuarioController <<controller>> {
    +consultarPerfil(usuarioId)
    +atualizarPreferencias(usuarioId, preferenciasDTO)
    +registrarLocalVisitado(usuarioId, lugarId)
    +listarLocaisVisitados(usuarioId)
  }

  class RoteiroController <<controller>> {
    +gerarRoteiro(dadosGeracaoDTO)
    +visualizarRoteiro(roteiroId)
    +ajustarRoteiro(roteiroId, ajusteDTO)
    +salvarRoteiro(roteiroId)
    +listarRoteiros(usuarioId)
    +excluirRoteiro(roteiroId)
  }

  class GrafoController <<controller>> {
    +cadastrarDestino(destinoDTO)
    +atualizarDestino(destinoId, destinoDTO)
    +removerDestino(destinoId)
    +adicionarLugar(destinoId, lugarDTO)
    +atualizarLugar(lugarId, lugarDTO)
    +removerLugar(destinoId, lugarId)
    +adicionarConexao(origemId, destinoId, conexaoDTO)
    +removerConexao(origemId, destinoId)
    +carregarGrafo(destinoId)
  }

  class LocalController <<controller>> {
    +visualizarDetalhesLocal(lugarId)
    +listarLocaisPorDestino(destinoId)
    +buscarLocaisPorCategoria(destinoId, categoria)
  }
}

package "Application Services" {

  class UsuarioService <<service>> {
    +consultarPerfil(usuarioId) : Usuario
    +atualizarPreferencias(usuarioId, preferencias) : Usuario
    +registrarLocalVisitado(usuarioId, lugarId) : void
    +listarLocaisVisitados(usuarioId) : List<Lugar>
  }

  class RoteiroService <<service>> {
    +gerarRoteiro(usuarioId, destinoId, orcamento, dias) : Roteiro
    +ajustarRoteiro(roteiroId, ajuste) : Roteiro
    -selecionarPontosViaveis(grafo, preferencias, orcamento, dias, locaisVisitados) : List<Lugar>
    -gerarRotaDiaria(hotelBase, pontosSelecionados, grafo) : List<ItemRoteiro>
    -recalcularRota(roteiro, grafo) : Roteiro
    -validarHorarioFuncionamento(lugar, horarioAtual) : boolean
    +salvarRoteiro(roteiroId) : void
    +listarRoteiros(usuarioId) : List<Roteiro>
    +buscarRoteiroPorId(roteiroId) : Roteiro
    +excluirRoteiro(roteiroId) : void
  }

  class GrafoService <<service>> {
    +cadastrarDestino(destino) : void
    +atualizarDestino(destinoId, destino) : void
    +removerDestino(destinoId) : void
    +adicionarLugar(destinoId, lugar) : void
    +atualizarLugar(lugarId, lugar) : void
    +removerLugar(destinoId, lugarId) : void
    +adicionarConexao(origemId, destinoId, conexao) : void
    +removerConexao(origemId, destinoId) : void
    +obterGrafoDestino(destinoId) : Grafo
    +salvarGrafo(destinoId) : void
  }

  class LocalService <<service>> {
    +buscarDetalhesLocal(lugarId) : Lugar
    +listarLocaisPorDestino(destinoId) : List<Lugar>
    +listarLocaisPorCategoria(destinoId, categoria) : List<Lugar>
  }
}

package "Domain" {

  class Usuario <<entity>> {
    -id : Long
    -nome : String
    -email : String
    -role : Role
    -preferencia : PreferenciaUsuario
    -roteiros : List<Roteiro>
    -locaisVisitados : List<Lugar>
    +atualizarPreferencias(preferencia)
    +registrarLugarVisitado(lugar)
  }

  class PreferenciaUsuario <<entity>> {
    -gostaGastronomia : boolean
    -gostaCultura : boolean
    -gostaLazer : boolean
    -gostaNatureza : boolean
    +atualizar(gastronomia, cultura, lazer, natureza)
  }

  class Roteiro <<entity>> {
    -id : Long
    -destino : Destino
    -hotelBase : Hotel
    -itens : List<ItemRoteiro>
    -salvo : boolean
    +adicionarItem(item)
    +removerItem(item)
    +calcularCustoTotal() : double
    +calcularTempoTotal() : double
    +marcarComoSalvo()
  }

  class ItemRoteiro <<entity>> {
    -ordem : int
    -tempoPermanencia : double
    -lugar : Lugar
    +alterarTempoPermanencia(tempo)
  }

  class Destino <<entity>> {
    -id : Long
    -nome : String
    -descricao : String
    +atualizarDados(nome, descricao)
  }

  class Grafo <<entity>> {
    -destino : Destino
    -lugares : List<Lugar>
    -conexoes : List<Conexao>
    +adicionarLugar(lugar)
    +removerLugar(lugarId)
    +adicionarConexao(conexao)
    +removerConexao(origemId, destinoId)
    +buscarLugarPorId(lugarId) : Lugar
  }

  abstract class Lugar <<entity>> {
    -id : Long
    -nome : String
    -categoria : String
    -custoEstimado : double
    -tempoMedioPermanencia : double
    -horarioAbertura : Time
    -horarioFechamento : Time
    +atualizarDados(nome, custo, tempo, horarioAbertura, horarioFechamento)
  }

  class Hotel <<entity>>
  class Restaurante <<entity>>
  class PontoTuristico <<entity>>

  class Conexao <<entity>> {
    -origem : Lugar
    -destino : Lugar
    -distancia : double
    -tempoDeslocamento : double
    -custoDeslocamento : double
    +atualizarDados(distancia, tempo, custo)
  }

  enum Role {
    USER
    ADMIN
  }
}

package "Ports" {

  interface UsuarioRepositoryPort <<port>> {
    +salvar(usuario) : void
    +buscarPorId(usuarioId) : Usuario
    +atualizar(usuario) : void
  }

  interface RoteiroRepositoryPort <<port>> {
    +salvar(roteiro) : void
    +buscarPorId(roteiroId) : Roteiro
    +listarPorUsuario(usuarioId) : List<Roteiro>
    +excluir(roteiroId) : void
  }

  interface GrafoRepositoryPort <<port>> {
    +carregarPorDestino(destinoId) : Grafo
    +salvar(destinoId, grafo) : void
    +existeDestino(destinoId) : boolean
    +removerDestino(destinoId) : void
  }
}

package "Adapters Outbound" {

  class UsuarioRepository <<repository>> {
    +salvar(usuario) : void
    +buscarPorId(usuarioId) : Usuario
    +atualizar(usuario) : void
  }

  class RoteiroRepository <<repository>> {
    +salvar(roteiro) : void
    +buscarPorId(roteiroId) : Roteiro
    +listarPorUsuario(usuarioId) : List<Roteiro>
    +excluir(roteiroId) : void
  }

  class ArquivoGrafoRepository <<repository>> {
    +carregarPorDestino(destinoId) : Grafo
    +salvar(destinoId, grafo) : void
    +existeDestino(destinoId) : boolean
    +removerDestino(destinoId) : void
    +parseArquivo(caminho) : Grafo
    +serializarGrafo(grafo) : String
  }
}

' Controllers -> Services
UsuarioController --> UsuarioService
RoteiroController --> RoteiroService
GrafoController --> GrafoService
LocalController --> LocalService

' Services -> Ports
UsuarioService --> UsuarioRepositoryPort
UsuarioService --> GrafoRepositoryPort

RoteiroService --> UsuarioRepositoryPort
RoteiroService --> RoteiroRepositoryPort
RoteiroService --> GrafoRepositoryPort

GrafoService --> GrafoRepositoryPort
LocalService --> GrafoRepositoryPort

' Adapters -> Ports
UsuarioRepository ..|> UsuarioRepositoryPort
RoteiroRepository ..|> RoteiroRepositoryPort
ArquivoGrafoRepository ..|> GrafoRepositoryPort

' Domain relations
Usuario "1" -- "1" PreferenciaUsuario : define
Usuario "1" -- "0..*" Roteiro : cria
Usuario "1" -- "0..*" Lugar : visitou
Usuario --> Role : possui

Roteiro "1" -- "1" Destino : tem
Roteiro "1" -- "1" Hotel : hotelBase
Roteiro "1" -- "1..*" ItemRoteiro : composto por
ItemRoteiro "1" -- "1" Lugar : inclui

Grafo "1" -- "1" Destino : representa
Grafo "1" -- "0..*" Lugar : contem
Grafo "1" -- "0..*" Conexao : contem

Conexao "1" --> "1" Lugar : origem
Conexao "1" --> "1" Lugar : destino

Lugar <|-- Hotel
Lugar <|-- Restaurante
Lugar <|-- PontoTuristico

@enduml
```
