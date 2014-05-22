/*
 * This is an example build file that demonstrates how to use the build system for
 * require.js.
 *
 * THIS BUILD FILE WILL NOT WORK. It is referencing paths that probably
 * do not exist on your machine. Just use it as a guide.
 *
 *
 */

({
    appDir: "app/",
    baseUrl: "./",
    dir: "built/",
    optimize: "uglify",
    uglify: {
        toplevel: false,
        ascii_only: true,
        no_mangle: true
    }
})