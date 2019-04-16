# Strategy

This implementation of Strategy is headless, you'll have to
rely on unit tests. It does output a copy of the board after
every move for debugging purposes, though.

## Tests

Tests are stored in the `tests` folder, but ***do not run
JUnit tests under it directly!*** Use the `test.strategy.crmyers`
package instead, those are the tests that I wrote, the other package
(`test.strategy.gpollice`) contains the Beta strategy master tests,
and are labeled as such. Don't worry, I made sure to rename
TestBoard to TestBoardOld so it doesn't conflict with new files.

