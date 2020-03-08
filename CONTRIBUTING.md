# Contributing to MaterialDayPicker

Thanks for showing interest in MaterialDayPicker. We're more than happy to accept new contributors.

## Branching Structure

For this project we structure our branches following the [Gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) approach. We have two main branches in our repo: `release` and `dev`. `dev` is where active development happens. Whenever we want to deploy a new release we merge the latest `dev` branch into `release`. Every commit on `release` is built on CI and deployed to bintray on success.

## Merging Pull Requests

GitHub will allow you to merge once all required CI checks and reviews are completed. You can merge your branch yourself once GitHub allows you to.

## Feature Development and Low Priority Bug Fixes

1) Create a branch off of `dev` following the pattern: `feature/<FEATURE_NAME>`
2) Implement your feature or low priority bug fix
3) If your code introduces any new APIs update the projects `README.md` to provide usage details
4) Open a pull request to merge your code into the `dev` branch. Note: GitHub will default to merging into the `release` so you will need to update the target branch yourself
5) Merge when green

## High Priority Bug Fixes

Note this section only applies to bugs that must urgently be fixed and can't wait for a normal release cycle to ship.

1) Create a branch directly off of `release` following the pattern: `hotfix/<BUG_NAME>`
2) Implement your fix
3) Increment the minor version of the `publishVersion` in the project's root [`build.gradle`](./build.gradle#L14)
4) Update the "What's new" section of the project's `README.md` to include details about the bug fixed by the hotfix
5) Open a pull request to merge your code straight into `release`
6) Merge when green
7) Create a new GitHub release and include the release notes you added to the "What's new" section of the `README.md`
8) Now we need to get this hotfix back into the `dev` branch. To do this open a PR merging `release` into `dev`
9) Merge when green

## Creating a new Library Release

1) Create a branch off of `dev` following the pattern: `prepare-for-<RELEASE-NUMBER>`
2) Update the `publishVersion` of the library in the project's root [`build.gradle`](./build.gradle#L14)
3) Update the "What's new" section of the project's `README.md` to include this release's features/bug fixes
4) Verify any new APIs are documented in the readme
5) Open up a PR to merge the branch with the above changes into `dev`
6) Once merged open a new PR to merge `dev` into `release`
7) Once approved merge the PR and a new version of the library will be deployed
8) Create a new GitHub release on this repository and include the release notes you added to the "What's new" section of the readme