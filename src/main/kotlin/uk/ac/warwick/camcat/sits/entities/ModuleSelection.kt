package uk.ac.warwick.camcat.sits.entities

import javax.persistence.AttributeConverter

enum class ModuleSelection(val code: String) {
  /**
   * Module Selection Status (SES) code.
   * This field is validated by an entry in the Module Selection Status Table (SES) table.
   *
   * 3 possible values
   * C  -> COMPULSORY
   * O  -> OPTIONAL
   * CO -> OPTIONAL CORE
   */
  Compulsory("C"),
  Optional("O"),
  OptionalCore("CO");
}

class ModuleSelectionConverter : AttributeConverter<ModuleSelection, String> {
  override fun convertToEntityAttribute(dbData: String?): ModuleSelection {
    return dbData?.let { code -> ModuleSelection.values().find { it.code == code } }!!
  }

  override fun convertToDatabaseColumn(attribute: ModuleSelection?): String {
    return attribute?.code!!
  }
}
