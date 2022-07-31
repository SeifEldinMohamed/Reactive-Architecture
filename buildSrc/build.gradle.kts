plugins {
    `kotlin-dsl`
}

// dependency management by kotlin dsl
// advantages of this way, it centralized dependencies which help me in:
// 1- when i have more than one module in my app (multi-module) so i can link on all dependencies from one place
// 2- auto complete for writing dependencies in build.gradel