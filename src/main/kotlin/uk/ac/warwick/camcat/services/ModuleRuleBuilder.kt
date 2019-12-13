package uk.ac.warwick.camcat.services

import uk.ac.warwick.camcat.services.ModuleRuleExpression.Operator
import uk.ac.warwick.camcat.sits.entities.ModuleModuleRuleBody
import java.util.*

class ModuleRuleBuilder {
  fun build(rows: List<ModuleModuleRuleBody>): ModuleRuleGroup {
    val stack = Stack<ModuleRuleGroup>()

    stack.push(ModuleRuleGroup(operator(rows.find { it.apply?.toUpperCase() != "I" }!!), emptyList()))

    fun closeGroup() {
      if (stack.size > 1) {
        val child = stack.pop()
        val parent = stack.pop()
        stack.push(parent.copy(items = parent.items + child))
      }
    }

    rows
      .filter { it.formedModuleCollection != null }
      .forEach { row ->
        val moduleRuleCode = ModuleRuleCode(row.formedModuleCollection!!.code)

        val groupWithNextRow = row.apply?.toUpperCase() == "I"

        if (groupWithNextRow) {
          stack.push(ModuleRuleGroup(operator(row), listOf(moduleRuleCode)))
        } else {
          if (stack.peek().items.size > 1) closeGroup()

          val top = stack.pop()
          stack.push(top.copy(items = top.items + moduleRuleCode))
        }
      }

    while (stack.size > 1) closeGroup()

    return stack.first()
  }

  private fun operator(mmb: ModuleModuleRuleBody): Operator =
    if (mmb.operator?.toUpperCase() == "OR") Operator.OR else Operator.AND
}

interface ModuleRuleExpression {
  enum class Operator {
    AND, OR
  }
}

data class ModuleRuleGroup(val operator: Operator, val items: List<ModuleRuleExpression>) :
  ModuleRuleExpression

data class ModuleRuleCode(val moduleCode: String) : ModuleRuleExpression
