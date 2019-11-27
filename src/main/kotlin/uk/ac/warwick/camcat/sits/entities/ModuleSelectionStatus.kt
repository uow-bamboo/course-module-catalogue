package uk.ac.warwick.camcat.sits.entities

import javax.persistence.AttributeConverter

enum class ModuleSelectionStatus(val code: String) {
  Compulsory("C"),
  Optional("O"),
  OptionalCore("CO")
}

class ModuleSelectionConverter : AttributeConverter<ModuleSelectionStatus, String> {
  override fun convertToEntityAttribute(dbData: String?): ModuleSelectionStatus? =
    dbData?.let { code -> ModuleSelectionStatus.values().find { it.code == code } }

  override fun convertToDatabaseColumn(attribute: ModuleSelectionStatus?): String? = attribute?.code
}
