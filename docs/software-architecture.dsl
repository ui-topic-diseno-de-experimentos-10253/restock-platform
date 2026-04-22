workspace "Restock Web - Open" "Inventory management system for restaurant administrators and suppliers" {

    model {
        admin = person "Restaurant Administrator" "User who manages restaurant inventory"
        supplier = person "Supplier" "User who provides products to restaurants"

        restock = softwareSystem "Restock" "Inventory management system for restaurant administrators and suppliers" "INVENTORY" {
            
            landingPage = container "Web Application" "Delivers the static content and the SPA" "Angular, HTML, CSS" "Landing Page"
            frontendApp = container "Restock UI" "Main interface for restaurant admins and suppliers to manage inventory and orders in real time." "Angular, TypeScript" "Web Application (SPA)" {
                /* navbar */
            sidebarMenu = component "SideBar" "Persistent menu with logo and navigation options depending on user role." "Angular"
    
                dashboardModule = component "Dashboard Module" "Landing screen with KPIs, stock alerts and recent activity." "Angular + ngx-charts"
            inventoryModule = component "Inventory Module" "CRUD for inventory items, stock levels and alerts." "Angular"
            purchasesModule = component "Purchases Module" "Register new customSupply purchases, view history and filter." "Angular"
            ordersModule = component "Orders Module" "Track orders, change statuses and view order details." "Angular"
            productsModule = component "Products Module" "Manage products in the menu/catalog." "Angular"
            recipesModule = component "Recipes Module" "Manage ingredient relationships and consumption logic." "Angular"
            reportsModule = component "Reports Module" "Analytics and performance charts for suppliers and inventory." "Angular + ngx-charts"
            notificationsModule = component "Notifications Module" "Displays toast messages for confirmations, alerts and errors." "Angular + ngx-toastr"

            authModule = component "Authentication Module" "Login, logout and session token management." "Angular"
            profileModule = component "Profile Module" "Manage personal or business profile and preferences." "Angular"
    
                /* internos*/
                sidebarMenu -> dashboardModule   "Routes to Dashboard" "internal"
                sidebarMenu -> inventoryModule   "Routes to Inventory" "internal"
                sidebarMenu -> purchasesModule   "Routes to Purchases" "internal"
                sidebarMenu -> ordersModule      "Routes to Orders" "internal"
                sidebarMenu -> productsModule    "Routes to Products" "internal"
                sidebarMenu -> recipesModule     "Routes to Recipes" "internal"
                sidebarMenu -> reportsModule     "Routes to Reports" "internal"
                sidebarMenu -> profileModule     "Routes to Profile" "internal"
                sidebarMenu -> authModule        "Routes to Change Password" "internal"
                sidebarMenu -> notificationsModule "Routes to Notification" "internal"
            }
            database = container "Database" "Stores data related to users, inventory, orders, and transactions for the system." "Relational DB PostgreSQL" "Database"
            backendAPI = container "Web Service" "Exposes REST APIs to handle business logic, inventory operations, and integrations with external services." "Monorepo (NestJS, Spring Boot, Node.js)" "REST API" {
                identity = component "Identity Service" "Handles authentication, user profiles and subscription management" "NestJS, TypeScript"
                inventory = component "Inventory Service" "Manages inventory, stock levels and alerts" "NestJS, TypeScript"
                recipe = component "Recipe Service" "CRUD recipes and tracks ingredient consumption" "NestJS, TypeScript"
                purchase = component "Purchase Service" "Records and queries restaurant purchases of supplies" "Spring Boot, Java"
                productCatalog = component "ProductCatalog Service" "Manages products, activation status and reviews" "Spring Boot, Java"
                orderSvc = component "Order Service" "Handles order creation, updates and delivery tracking" "Spring Boot, Java"
                supplierAnalytics = component "SupplierAnalytics Service" "Generates sales history, invoices and best‑customer reports" "Node.js, JavaScript"
    
                identity -> database "Reads and writes user and auth data" "JDBC"
                inventory -> database "Reads and writes inventory data" "JDBC"
                productCatalog -> database "Reads and writes product data" "JDBC"
                orderSvc -> database "Reads and writes order data" "JDBC"
    
                recipe -> inventory "Updates stock on recipe consumption"
                purchase -> inventory "Updates stock on customSupply purchase"
                orderSvc -> productCatalog "Reads product details for orders"
                orderSvc -> supplierAnalytics "Publishes order events"
                }

            
            admin -> landingPage "Visits restock.com" "HTTPS"
            supplier -> landingPage "Visits restock.com" "HTTPS"
            
            admin -> frontendApp "Uses to manage and track inventory"
            supplier -> frontendApp "Uses to customSupply and manage stock availability"

            landingPage -> frontendApp "Delivers to the web browser"
            frontendApp -> identity "Authenticates and manages profile" "JSON/HTTPS"
            frontendApp -> inventory "Manages stock and alerts" "JSON/HTTPS"
            frontendApp -> recipe "Manages recipes" "JSON/HTTPS"
            frontendApp -> purchase "Registers purchases of supplies" "JSON/HTTPS"
            frontendApp -> productCatalog "Manages products and reviews" "JSON/HTTPS"
            frontendApp -> orderSvc "Places and tracks orders" "JSON/HTTPS"
            frontendApp -> supplierAnalytics "Views sales reports" "JSON/HTTPS"
            backendAPI -> database "Reads from and writes to" "SQL/TCP"
        
            /* llamar al backend */
            authModule -> identity "Change password" "JSON/HTTPS"
            profileModule -> identity "Get/Update user profile" "JSON/HTTPS"
            inventoryModule -> inventory "Get/Update stock, subscribe alerts" "JSON/HTTPS"
            purchasesModule -> purchase "Create purchase, list history" "JSON/HTTPS"
            ordersModule -> orderSvc "List orders, update statuses" "JSON/HTTPS"
            productsModule -> productCatalog "CRUD products" "JSON/HTTPS"
            recipesModule -> recipe "CRUD recipes, calculate usage" "JSON/HTTPS"
            reportsModule -> supplierAnalytics "Retrieve sales & client analytics" "JSON/HTTPS"
            dashboardModule -> inventory "Fetches low-stock alerts" "internal"
            dashboardModule -> orderSvc "Fetches recent orders summary" "internal"
            dashboardModule -> supplierAnalytics "Fetches KPI data" "internal"
        }

        resendAPI = softwareSystem "Resend API" "External service used to send transactional emails such as order confirmations and notifications." "Resend API"
        cloudinaryAPI = softwareSystem "Cloudinary API" "External service used for uploading, storing, and optimizing images and other media files." "Cloudinary API"

        backendAPI -> resendAPI "Makes API calls to send transactional emails like order confirmations and notifications" "JSON/HTTPS"
        backendAPI -> cloudinaryAPI "Makes API calls to upload, retrieve, and manage media files such as product images" "JSON/HTTPS"
        
         identity -> resendAPI "Sends password reset and notification emails" "JSON/HTTPS"
         identity -> cloudinaryAPI "Uploads profile images" "JSON/HTTPS"
         productCatalog -> cloudinaryAPI "Uploads product images" "JSON/HTTPS"

        
        resendAPI -> admin "Sends transactional emails such as order confirmations, password resets, and notifications."
        
        /* para llamar a appis*/
        notificationsModule -> resendAPI "Send transactional emails (confirmations, alerts)" "JSON/HTTPS"
        profileModule -> cloudinaryAPI "Upload profile images" "JSON/HTTPS"
        recipesModule -> cloudinaryAPI "Upload product images" "JSON/HTTPS"
    }

    views {
        systemContext restock {
            include *
            autolayout
        }

        container restock {
            include *
            autolayout
        }
        
        component backendAPI {
            include *  
            autolayout
        }
        component frontendApp {
            include *
            
        }


        styles {
            element "Person" {
                shape "Person"
                background "#08427b"
                color "#ffffff"
            }

            element "INVENTORY" {
                shape "RoundedBox"
                background "#28a745"
                color "#ffffff"
            }

            element "Database" {
                shape "Cylinder"
                background "#ec0e0e"
                color "#ffffff"
            }

            element "Landing Page" {
                background "#ffc107"
                shape "Box"
            }

            element "Web Application (SPA)" {
                background "#007bff"
                shape "WebBrowser"
            }

            element "REST API" {
                background "#17a2b8"
                shape "Box"
            }

            element "Resend API" {
                background "#878680"
                shape "Box"
            }

            element "Cloudinary API" {
                background "#878680"
                shape "Box"
            }

        }

        theme default
    }
}