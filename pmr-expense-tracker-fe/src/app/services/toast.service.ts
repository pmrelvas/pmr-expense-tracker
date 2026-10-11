import { Injectable, inject } from '@angular/core'
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar'

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private readonly snackBar = inject(MatSnackBar)

  private readonly baseConfig: MatSnackBarConfig = {
    duration: 5000,
    horizontalPosition: 'right',
    verticalPosition: 'top'
  }

  success(message: string): void {
    this.snackBar.open(message, 'Close', {
      ...this.baseConfig,
      panelClass: 'toast-success'
    })
  }

  error(message: string): void {
    this.snackBar.open(message, 'Close', {
      ...this.baseConfig,
      duration: 8000,
      panelClass: 'toast-error'
    })
  }
}
