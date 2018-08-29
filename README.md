# NovaUtils

NovaUtils is the main API and library behind all DreamExposure projects.

It helps us maintain large amounts of code without the need to rewrite functions over and over again.

Join our discord server: https://discord.gg/2TFqyuy

Javadoc: https://docs.dreamexposure.org/projects/novautils/

Wiki with WAY more info: https://wiki.novamaday.com/projects/novautils/start



## Current Features


## Planned and In-Progress Features
- Database API for easy connections to MySQL databases.
- File/folder utilities for convenience.
- SQLite support

## Issues
Should you find an issue with NovaUtils, please create a new issue in the Issues pages on this repository or via Dev Bukkit with a proper ticket.

## Use in Your Plugins
Instructions will be provided in the Wiki

## Contributing
1. Fork this repo and make changes in your own copy
2. Add a test if applicable and run the existing tests with `mvn clean test` to make sure they pass
3. Commit your changes and push to your fork `git push origin master`
4. Create a new pull request and submit it back to us!

### Deprecation Policy

NovaUtils will use the following policy for deprecation:

- All Minor.patch versions under the same Major will be compatible
- SNAPSHOT builds may be unstable and/or change many times before official release. Use these builds at your own risk.
- Deprecated classes and methods will be available until the next Major release.