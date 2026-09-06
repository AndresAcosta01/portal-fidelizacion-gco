import { Routes } from '@angular/router';

import { Inicio } from './features/inicio/inicio';
import { MainLayout } from './layouts/main-layout/main-layout';

export const routes: Routes = [
    {
        path: '',
        component: MainLayout,
        children: [
            {
                path: '',
                component: Inicio
            }
        ]
    }
];