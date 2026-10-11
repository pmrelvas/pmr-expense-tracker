import { Component, ChangeDetectionStrategy } from '@angular/core'
import { RouterLink, RouterLinkActive } from '@angular/router'

import { MatToolbarModule } from '@angular/material/toolbar'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, MatToolbarModule, MatButtonModule, MatIconModule],
  templateUrl: './nav.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './nav.scss'
})
export class NavComponent {}
