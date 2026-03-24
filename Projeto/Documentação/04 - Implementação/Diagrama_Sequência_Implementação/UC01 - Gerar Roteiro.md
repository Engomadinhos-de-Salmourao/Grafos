# UC01 - Gerar Roteiro
---

![](https://www.plantuml.com/plantuml/png/ZLN1Rjj64BtlLmpYYmJ8QDqWWLCKGMJ9IWq8g14dFVKoNewagPSxyknIXb3qOueU-Y7-iIv9hosBH40A0eZPD--zURiaFuM86glIcSZH4NoR_NW6f_2P51LK8h4A50GxnZML1gielYflIOq9T-otg5X2s60X3-pNi48Nw615fVA55VMaSFRJOGlTL-GjmLy6OAeg3rytREyyXG1dgRJa5ItPquLeGJeJ9med7vhhS1NjtRhHU48iFjI_XBX6ZMnvYpw-edZVNAy12_7n9kuTmQsuugZvIE5Xmv7CtmUJEFOKzri2qRWHOz36XFiMAbGME9c_lvmkpirjJO6wXozjfhEagU8SQOAyMxoxV6jiMyg86z8TMqeppHPlPukyd9c_q5O29wbzGapd5_FpPyHKxolUoMV5LH-CVEIuXpTFH6y0GoQTIudiuUJoV3wzN6HaGtHKp2wVooPFVFgnPyQVOKsA-hGmAh2GK9ynYpB_-jlOT3eIpL4GnnnLgtTLJA2W4Datjw8MosHHKfKnZ8qvOacydGGJzMqLBEgrw5NnmZmsNUXfZx7Cs2VMsOoicEIKretclFK_odCauVwsVlKQrATzjAjrVl5QQKLAtZAEydWPq4j-qD2YXoejnIA7tpavhtTxcDgIG_nbMQrHF_R9TF2175aMNyVeeoGvt167qRgEP0BR5_DXyeS_EXmJSAyzZZkIVPhRHkacAwU-GLQvICgyOelwQrfp5nWe--m-3mDyR3BB8FdyxE2SKHb7pzqfb-v44sYS3JF2aNnugXhZHBQmGyS5Flxt-6_pzR1OO9iRf1kLY0PWWEcM9kMbg2oBojV-sYqSkVgKJ63JharZXMwSMD75PbiThCZMtoMG2jZl-F6V7Ra462ICVrOXejwqNod67vHAsP5UHIf7jGpvmgJ_Tw9FB5OP5QoK-GqrUTaqe3QtJD0VhRQ5-cWKqXNhVzVcMjzRzULPxR1_IlDKfViV)
---
```plantuml
@startuml
autonumber

skinparam shadowing false
skinparam roundcorner 12
skinparam sequence {
  ArrowThickness 1
  LifeLineBorderColor #999999
  LifeLineBackgroundColor #F9F9F9
  ParticipantBorderColor #666666
  ParticipantFontStyle bold
  ParticipantBackgroundColor white
}
skinparam linetype ortho

actor Usuario #D6EAF8
boundary RoteiroController #D5F5E3
control RoteiroService #FCF3CF
control UsuarioRepository #FADBD8
control ArquivoGrafoRepository #FADBD8
entity "Usuario" as UsuarioDomain #E8DAEF
entity Grafo #E8DAEF
entity Roteiro #E8DAEF

Usuario -> RoteiroController : gerarRoteiro(dadosGeracaoDTO)
RoteiroController -> RoteiroService : gerarRoteiro(usuarioId, destinoId, orcamento, dias)

RoteiroService -> UsuarioRepository : buscarPorId(usuarioId)
UsuarioRepository --> RoteiroService : Usuario

RoteiroService -> ArquivoGrafoRepository : carregarPorDestino(destinoId)
ArquivoGrafoRepository --> RoteiroService : Grafo

RoteiroService -> UsuarioDomain : getPreferencia()
UsuarioDomain --> RoteiroService : PreferenciaUsuario

RoteiroService -> UsuarioDomain : getLocaisVisitados()
UsuarioDomain --> RoteiroService : List<Lugar>

RoteiroService -> RoteiroService : selecionarPontosViaveis(grafo, preferencias,\norcamento, dias, locaisVisitados)
RoteiroService --> RoteiroService : pontosSelecionados

RoteiroService -> RoteiroService : identificarHotelBase(grafo, dadosGeracaoDTO)
RoteiroService --> RoteiroService : hotelBase

RoteiroService -> RoteiroService : gerarRotaDiaria(hotelBase,\npontosSelecionados, grafo)
RoteiroService --> RoteiroService : itensRoteiro

loop validação de cada ponto da rota
  RoteiroService -> RoteiroService : validarHorarioFuncionamento(lugar, horarioAtual)
  alt local fechado ou inviável
    RoteiroService -> RoteiroService : ajustarSequencia/removerItem()
  end
end

RoteiroService -> Roteiro : criar com destino, hotelBase e itens
Roteiro --> RoteiroService : roteiroGerado

RoteiroService --> RoteiroController : Roteiro
RoteiroController --> Usuario : roteiroGerado
@enduml
```
