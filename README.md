# blaseball-api-scala
library for interacting with blaseball.com

mostly isnt here

## use
`sbt publishLocal`

## actual use
```scala
import dev.taylorh.blaseball.api._
import dev.taylorh.blaseball.util._

implicit val api: BlaseballApi = new BlaseballApiCache(BlaseballApiNoCache)

val players = BlaseballUtil.getNonShadowPlayersInLeague(BlaseballConstants.internetBlaseballLeagueId)

val lowe = players.find(_.name == "Lowe Forbes").get
println(lowe)
```
```
Player(b348c037-eefc-4b81-8edd-dfa96188a97e,Lowe Forbes,PlayerAttributes(Set(TRIPLE_THREAT, ELSEWHERE),Set(),Set(),Set()),BaserunningAttributes(1.0076577795404305,0.7376708329124532,0.65778038325191,0.31668496987719963,0.6846554366890226),DefenseAttributes(0.5649882368120599,0.6646997379187309,0.46568263804703275,1.0692973515494772,0.7641772466540312),HittingAttributes(0.5575745289604013,0.5598360608235193,0.29906780048711734,1.1529624468864095,1.0645705544262631,0.7032875904768319,0.29009409249364415,0.001),PitchingAttributes(0.5340845817870057,0.4966827262567918,0.5524452849430901,1.4607113410346162,1.3875668180863707,1.1567138528908278,12),OtherAttributes(false,false,0.40039817421122226,1.0586070350960344,4,Some(Eating fans),Some(2),Some(8),73,27.10321071373309,0),TeamAttributes(a37f9158-7f82-46bc-908c-c9e2dda7c33b,None),Ratings(0.6866326251134355,0.4937555250856056,0.7677566894190615,0.7808852623437675,0,0))
```

## checklist
 - [ ] players
   - [x] stats ~~(i think they added new ones?)~~ they didnt im just forgetful
   - [ ] feed
     - [ ] metadata
     - [x] everything else
   - [ ] items
 - [ ] teams
   - [ ] probably needs restructure
 - [x] league, subleague, division
 - [ ] games
 - [ ] docs
