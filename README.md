[![deploy-on-pr-merge](https://github.com/matsim-org/matsim-libs/actions/workflows/deploy-on-pr-merge.yaml/badge.svg?event=pull_request_target&branch= "PR-merge release")](https://github.com/matsim-org/matsim-libs/actions/workflows/deploy-on-pr-merge.yaml)
[![deploy-weekly](https://github.com/matsim-org/matsim-libs/actions/workflows/deploy-weekly.yaml/badge.svg "weekly release")](https://github.com/matsim-org/matsim-libs/actions/workflows/deploy-weekly.yaml)
[![full-integration](https://github.com/matsim-org/matsim-libs/actions/workflows/full-integration.yaml/badge.svg "daily release-like build")](https://github.com/matsim-org/matsim-libs/actions/workflows/full-integration.yaml)
[![weekly-code-coverage](https://github.com/matsim-org/matsim-libs/actions/workflows/code-coverage.yaml/badge.svg "matsim module only")](https://github.com/matsim-org/matsim-libs/actions/workflows/code-coverage.yaml)
[![windows-build](https://ci.appveyor.com/api/projects/status/fwsgslb0kl06q36d/branch/master?svg=true)](https://ci.appveyor.com/project/michalmac/matsim-libs/branch/master "Windows build")
[![codecov - matsim only](https://codecov.io/gh/matsim-org/matsim-libs/branch/master/graph/badge.svg?token=3p7uJdHdnd "matsim module only")](https://codecov.io/gh/matsim-org/matsim-libs)

## Overview

MATSim provides a toolbox to run and implement large-scale agent-based
transport simulations. The toolbox consists of several modules which can be
combined or used stand-alone. Modules can be replaced by own implementations
to test single aspects of your own work. Currently, MATSim offers a toolbox
for demand-modeling, agent-based mobility-simulation (traffic flow simulation),
re-planning, a controller to iteratively run simulations as well as methods to
analyze the output generated by the modules.

All list of available extensions can be found in the [contribs](contribs/README.md) folder.

For more information, see the project website at http://www.matsim.org/.

## Questions

Have any questions? Visit https://matsim.org/faq
and see if it has already been answered. If not, post a new question.

## Development

The issue tracker is at https://matsim.org/issuetracker .

Build by running (from this directory, not any sub-directory):

```
mvn package -DskipTests
```

if you want to install the core only you can run

```
mvn install --also-make --projects matsim
```


## Binaries

Releases (also PR-based and weekly releases) and snapshots can be found at https://repo.matsim.org/.

An example how to get started with your own project, including a complete pom.xml, is at https://github.com/matsim-org/matsim-example-project .

Code examples are at https://github.com/matsim-org/matsim-code-examples.



