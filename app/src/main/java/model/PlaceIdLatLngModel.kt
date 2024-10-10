package model

data class PlaceIdLatLngModel(
    val html_attributions: List<Any> = listOf(),
    val result: Result = Result(),
    val status: String = ""
) {
    data class Result(
        val address_components: List<AddressComponent> = listOf(),
        val adr_address: String = "",
        val formatted_address: String = "",
        val geometry: Geometry = Geometry(),
        val icon: String = "",
        val icon_background_color: String = "",
        val icon_mask_base_uri: String = "",
        val name: String = "",
        val place_id: String = "",
        val plus_code: PlusCode = PlusCode(),
        val reference: String = "",
        val types: List<String> = listOf(),
        val url: String = "",
        val utc_offset: Int = 0,
        val vicinity: String = ""
    ) {
        data class AddressComponent(
            val long_name: String = "",
            val short_name: String = "",
            val types: List<String> = listOf()
        )

        data class Geometry(
            val location: Location = Location(),
            val viewport: Viewport = Viewport()
        ) {
            data class Location(
                val lat: Double = 0.0,
                val lng: Double = 0.0
            )

            data class Viewport(
                val northeast: Northeast = Northeast(),
                val southwest: Southwest = Southwest()
            ) {
                data class Northeast(
                    val lat: Double = 0.0,
                    val lng: Double = 0.0
                )

                data class Southwest(
                    val lat: Double = 0.0,
                    val lng: Double = 0.0
                )
            }
        }

        data class PlusCode(
            val compound_code: String = "",
            val global_code: String = ""
        )
    }
}