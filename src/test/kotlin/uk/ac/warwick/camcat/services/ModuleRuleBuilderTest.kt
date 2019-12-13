package uk.ac.warwick.camcat.services

import org.junit.Assert.assertEquals
import org.junit.Test
import uk.ac.warwick.camcat.factories.ModuleModuleRuleBodyFactory
import uk.ac.warwick.camcat.services.ModuleRuleExpression.Operator

class ModuleRuleBuilderTest {
  private val builder = ModuleRuleBuilder()

  @Test
  fun testOneModule() {
    val result = builder.build(
      listOf(
        ModuleModuleRuleBodyFactory(
          sequence = "001",
          apply = null,
          operator = null,
          otherModuleCode = "CS101-15"
        ).build()
      )
    )

    val expected = ModuleRuleGroup(
      Operator.AND, listOf(
        ModuleRuleCode("CS101-15")
      )
    )

    assertEquals(expected, result)
  }

  @Test
  fun testTwoModules() {
    val result = builder.build(
      listOf(
        ModuleModuleRuleBodyFactory(
          sequence = "001",
          apply = null,
          operator = null,
          otherModuleCode = "CS101-15"
        ).build(),
        ModuleModuleRuleBodyFactory(
          sequence = "002",
          apply = null,
          operator = null,
          otherModuleCode = "CS102-15"
        ).build()
      )
    )

    val expected = ModuleRuleGroup(
      Operator.AND, listOf(
        ModuleRuleCode("CS101-15"),
        ModuleRuleCode("CS102-15")
      )
    )

    assertEquals(expected, result)
  }

  @Test
  fun test() {
    val result = builder.build(
      listOf(
        ModuleModuleRuleBodyFactory(
          sequence = "001",
          apply = "I",
          operator = "OR",
          otherModuleCode = "CS101-15"
        ).build(),
        ModuleModuleRuleBodyFactory(
          sequence = "002",
          apply = null,
          operator = "AND",
          otherModuleCode = "CS102-15"
        ).build(),
        ModuleModuleRuleBodyFactory(
          sequence = "003",
          apply = null,
          operator = null,
          otherModuleCode = "CS103-15"
        ).build()
      )
    )

    val expected = ModuleRuleGroup(
      Operator.AND, listOf(
        ModuleRuleGroup(
          Operator.OR, listOf(
            ModuleRuleCode("CS101-15"),
            ModuleRuleCode("CS102-15")
          )
        ),
        ModuleRuleCode("CS103-15")
      )
    )

    assertEquals(expected, result)
  }
}


