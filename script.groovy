def buildApp() {
    echo 'building the application...'
} 

def testApp() {
    echo 'testing the application...'
} 

def deployApp() {
    echo 'deplying the application...'
    echo "deploying version ${params.VERSION}"
} 

def validateApp() {
    echo 'validating the application...'
    echo "validating version ${params.VERSION}"
} 

return this
