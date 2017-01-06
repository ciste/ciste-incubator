#!groovy

def clojure

// Set build properties
properties([[$class: 'GithubProjectProperty',
               displayName: 'Ciste Incubator',
               projectUrlStr: 'https://github.com/duck1123/ciste-incubator/']])

stage('Prepare environment') {
    node('docker') {
        clojure = docker.image('clojure')
        clojure.pull()

        sh 'env | sort'
    }
}

// stage('Unit Tests') {
//     node('docker') {
//         clojure.inside {
//             checkout scm
//             wrap([$class: 'AnsiColorBuildWrapper']) {
//                 sh 'lein midje'
//             }
            
//             step([$class: 'JUnitResultArchiver', testResults: 'target/surefire-reports/TEST-*.xml'])
//         }
//     }
// }

stage('Generate Reports') {
    node('docker') {
        clojure.inside {
            checkout scm
            sh 'lein doc'
            step([$class: 'JavadocArchiver', javadocDir: 'doc', keepAll: true])
            step([$class: 'TasksPublisher', high: 'FIXME', normal: 'TODO', pattern: '**/*.clj,**/*.cljs'])
        }
    }
}


// TODO: Skip for features and PRs
//stage('Deploy Artifacts') {
//    node('docker') {
//        clojure.inside {
//            withCredentials([[$class: 'UsernamePasswordMultiBinding',
//                                credentialsId: 'repo-creds',
//                                usernameVariable: 'REPO_USERNAME', passwordVariable: 'REPO_PASSWORD']]) {
//                sh 'lein deploy'
//            }
//        }
//    }
//}
