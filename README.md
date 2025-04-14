## Preconditions
Description will be here

### TODO
1) ~~Implement syntax check for statements. Syntax errors should contain 
    location of errors and their descriptions~~
2) Implement default values for template variables. `{someObject.someField?:nika} !IS NULL`
3) ~~Aliases for existing preconditions~~
4) Extend core module with more basic translators (< <= >= xor)
5) Create dates module
6) ~~Create editor (Ideal would be to implement it intellij or vscode plugin). Simple textarea can work as poc~~
7) Add an extension point for parameter interpretation logic defined in `PlainStatementParameter` probably by introducing functions like `{card.availableAmount} == bigDecimal('2.20')`