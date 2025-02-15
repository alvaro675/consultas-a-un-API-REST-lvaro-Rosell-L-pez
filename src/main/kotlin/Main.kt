import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val address: Address,
    val birthDate: String,
    val company: Company,
    val email: String,
    val firstname: String,
    val id: Int,
    val lastname: String,
    val login: Login,
    val phone: String,
    val website: String
)
@Serializable
data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)
@Serializable
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)
@Serializable
data class Login(
    val md5: String,
    val password: String,
    val registered: String,
    val sha1: String,
    val username: String,
    val uuid: String
)
@Serializable
data class Geo(
    val lat: String,
    val lng: String
)
fun main() {

    //  crear cliente http
    val client = HttpClient.newHttpClient()

    // crear solicitud
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://jsonplaceholder.org/users"))
        .GET()
        .build()

    //  Enviar la solicitud con el cliente
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    // obtener string con datos
    val jsonBody = response.body()

    // Deserializar el JSON a una lista de objetos User
    val users: List<User> = Json.decodeFromString(jsonBody)

    //println(users)

    // Imprimir los usuarios con diversos campos
   println("Lista de usuarios con dirección y coordenadas:")
    users.forEach { user ->
        println(
          "Nombre: ${user.firstname} ${user.lastname}, Email: ${user.email}, Dirección: ${user.address.street}, ${user.address.city}, ${user.address.zipcode}, " +
                   "Coordenadas: (${user.address.geo.lat}, ${user.address.geo.lng})"

       )
    }
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
    println("Usuarios del zipcode 12345-6789")
    users.filter { user -> user.address.zipcode=="12345-6789"   }.sortedBy { user -> user.lastname   }.forEach{ user ->
            println("Nombre:  ${user.lastname} ${user.firstname}, Email: ${user.email}, Dirección: ${user.address.street}")
        }
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
    println("Usuarios ordenados por el company.bs")
    users.sortedBy { user -> user.company.bs }.forEach{user -> println( "Nombre: ${user.firstname} ${user.lastname}, Company"+user.company.name+ " Catch Phrase: "+user.company.catchPhrase+" bs: "+user.company.bs) }
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
    println("Compañias de los usuarios ordenados por el zipcode")
    users.sortedBy { user -> user.address.zipcode }.forEach{user -> println("Company ${user.company.name}  Catch Phrase: ${user.company.catchPhrase} bs: ${user.company.bs}") }
    println("---------------------------------------------------------------------------------------------------------------------------------------------")
    println("contraseña y nombre de Doe ")
    users.filter { user -> user.firstname=="Doe" }.forEach{user -> println(user.login.password+""+user.login.username) }
}