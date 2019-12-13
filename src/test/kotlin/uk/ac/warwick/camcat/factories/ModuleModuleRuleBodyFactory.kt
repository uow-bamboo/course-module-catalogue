package uk.ac.warwick.camcat.factories

import uk.ac.warwick.camcat.sits.entities.*
import uk.ac.warwick.util.termdates.AcademicYear

data class ModuleModuleRuleBodyFactory(
  var moduleCode: String = "CS126-15",
  var moduleRuleCode: String = "001",
  var academicYear: AcademicYear = AcademicYear.starting(2020),
  var ruleType: String = "PRE",
  var sequence: String = "001",
  var ruleTakingFlag: String = "P",
  var min: Int = 1,
  var max: Int = 1,
  var operator: String? = null,
  var apply: String? = null,
  var selectionData: String = "M",
  var otherModuleCode: String = "CS118-15"
) {
  fun build(): ModuleModuleRuleBody =
    ModuleModuleRuleBody(
      ModuleModuleRuleBodyKey(
        moduleCode = moduleCode,
        moduleModuleRuleCode = moduleRuleCode,
        sequence = sequence
      ),
      ruleTakingFlag = ruleTakingFlag,
      min = min,
      max = max,
      operator = operator,
      apply = apply,
      selectionData = selectionData,
      formedModuleCollection = FormedModuleCollection(
        code = otherModuleCode,
        inUse = true,
        name = null,
        pathwayDietModules = emptyList(),
        shortName = null,
        formedModuleCollectionElements = emptyList(),
        moduleRuleElements = emptyList()
      ),
      rule = ModuleModuleRule(
        key = ModuleModuleRuleKey(moduleCode, moduleRuleCode),
        academicYear = academicYear,
        type = RuleType(ruleType, null),
        ruleClass = null,
        description = null,
        elements = emptyList(),
        module = null
      )
    )
}
