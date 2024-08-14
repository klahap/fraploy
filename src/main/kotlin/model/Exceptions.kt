package io.github.klahap.fraploy.model

import okhttp3.HttpUrl


class FrappeCloudApiException(url: HttpUrl, code: Int, message: String) :
    Exception("error calling frappe cloud api ($url), code: $code, message: $message")

class FrappeAppNotExistsException(name: AppName) :
    Exception("app '$name' not exists")

class FrappeAppReleaseNotExistsException(name: AppName, version: GitVersion) :
    Exception("no release for app '$name' with version '$version' found")

class ReleaseGroupNotFoundException(title: String) :
    Exception("no release group for title '$title' found")

class MultipleReleaseGroupsFoundException(title: String) :
    Exception("multiple release groups for title '$title' found")

class ErrorDuringDeploymentException :
    Exception("error during deployment, see Frappe Cloud logs for more information")
